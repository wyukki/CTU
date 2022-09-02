#include "ec.h"
#include "ptab.h"
#include "string.h"

typedef enum {
    sys_print = 1,
    sys_sum = 2,
    sys_break = 3,
    sys_thr_create = 4,
    sys_thr_yield = 5,
} Syscall_numbers;

mword prevAddress = 0;

void Ec::syscall_handler(uint8 a) {
    // Get access to registers stored during entering the system - see
    // entry_sysenter in entry.S
    Sys_regs *r = current->sys_regs();
    Syscall_numbers number = static_cast<Syscall_numbers>(a);

    switch (number) {
        case sys_print: {
            char *data = reinterpret_cast<char *>(r->esi);
            unsigned len = r->edi;
            for (unsigned i = 0; i < len; i++)
                printf("%c", data[i]);
            break;
        }
        case sys_sum: {
            // Naprosto nepotřebné systémové volání na sečtení dvou čísel
            int first_number = r->esi;
            int second_number = r->edi;
            r->eax = first_number + second_number;
            break;
        }
        case sys_break: {
            mword prev = Ec::current->break_current;
            bool error = false;
            mword addr = r->esi;
            if (!prevAddress) {
                prevAddress = Ec::break_min;
            }
            if (addr == 0) {
                r->eax = prevAddress;
                break;
            } else if (addr < Ec::break_min) {
                printf("Error: cannot set the break point to addres, that is lower than starting address. The address in argument = %lu!\n", r->esi);
                r->eax = 0;
                break;
            } else if (addr > 0xBFFFF000) {
                printf("Error: cannot set the break point to address, that is greater than 0xBFFFF000!\n");
                r->eax = 0;
                break;
            }
            if (prevAddress % PAGE_SIZE) {
                mword offset = prevAddress % PAGE_SIZE;
                mword virt = reinterpret_cast<mword>(Kalloc::phys2virt(Ptab::get_mapping(prev - PAGE_SIZE) & ~PAGE_MASK));
                memset(reinterpret_cast<void *>(virt  + offset), 0, PAGE_SIZE - offset);
            }
            if (addr > prev) {
                long int diff = addr - prev;
                int allocatedPages = 0;
                while (diff > 0) {
                    void *mem;
                    if (!(mem = Kalloc::allocator.alloc_page(1, Kalloc::FILL_0))) {
                        printf("\nAllocate page error!\n\n");
                        r->eax = 0;
                        error = true;
                    }
                    if (!error && !Ptab::insert_mapping(prev + (allocatedPages * PAGE_SIZE), Kalloc::virt2phys(mem), Ptab::RW | Ptab::PRESENT | Ptab::USER)) {
                        printf("\nError: insert maping error!\n\n");
                        error = true;
                        Kalloc::allocator.free_page(mem);
                        r->eax = 0;
                    }
                    if (error) {  // free already allocated pages
                        printf("\nAn error occured, adrr in argument = %lu, currBreak = %lu!\n\n", addr, prev);
                        for (int i = 0; i < allocatedPages; ++i) {
                            mword virt = prev + (i * allocatedPages);
                            if (!Ptab::insert_mapping(virt, Ptab::get_mapping(virt), 0)) {
                                printf("Insert mapping error!\n");
                                r->eax = 0;
                                break;
                            }
                            Kalloc::allocator.free_page(
                                Kalloc::phys2virt(
                                    Ptab::get_mapping(prev + (i * PAGE_SIZE)) & ~PAGE_MASK));
                        }
                        break;
                    }
                    diff -= PAGE_SIZE;
                    allocatedPages++;
                }
            } else {  // Free memory
                mword diff = prev - addr;
                int freedPages = 1;
                int pagesToFree = diff / PAGE_SIZE + 1;
                while (freedPages != pagesToFree) {
                    mword virt = prev - (freedPages * PAGE_SIZE);
                    if (!Ptab::insert_mapping(virt, Ptab::get_mapping(virt), 0)) {
                        printf("Insert mapping error!\n");
                        r->eax = 0;
                        break;
                    }
                    Kalloc::allocator.free_page(
                        Kalloc::phys2virt(
                            Ptab::get_mapping(virt) & ~PAGE_MASK));
                    diff -= PAGE_SIZE;
                    freedPages++;
                }
            }
            if (error) {
                break;
            }
            if (addr % PAGE_SIZE != 0) {
                Ec::current->break_current = addr - (addr % PAGE_SIZE) + PAGE_SIZE;
            } else {
                Ec::current->break_current = addr;
            }
            r->eax = prevAddress;
            prevAddress = r->esi;
            break;
        }
        default:
            printf("unknown syscall %d\n", number);
            break;
    };
    // printf("DONE!\n");
    ret_user_sysexit();
}
