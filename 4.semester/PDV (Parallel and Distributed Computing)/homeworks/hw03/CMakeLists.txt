cmake_minimum_required(VERSION 3.15)
project(PDV_HW03)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_BUILD_TYPE "Release")

if(MSVC)
	add_compile_options("/W4")
	add_compile_options("/O2")
else()
	add_compile_options("-Wall" "-Wextra" "-g")
	add_compile_options("-O3")
endif()

find_package(OpenMP REQUIRED)

add_executable(PDV_HW03 main.cpp tests.h bst_tree.cpp bst_tree.h)

target_link_libraries(PDV_HW03 PUBLIC OpenMP::OpenMP_CXX)