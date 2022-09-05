cmake_minimum_required(VERSION 3.15)
project(02_Threadpool)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_BUILD_TYPE "Release")

if(MSVC)
	add_compile_options("/W4")
	add_compile_options("/O2")
else()
	add_compile_options("-Wall" "-Wextra")
	add_compile_options("-O3")
endif()

find_package(Threads REQUIRED)

add_executable(threadpool.bin main.cpp)

target_link_libraries(threadpool.bin PUBLIC Threads::Threads)

