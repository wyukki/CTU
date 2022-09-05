cmake_minimum_required(VERSION 3.15)
project(ParallelSortingHW)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_BUILD_TYPE "Release")
if(MSVC)
	add_compile_options("/W4")
	add_compile_options("/O2")
else()
	add_compile_options("-Wall" "-Wextra" "-g")
	add_compile_options("-O3")
endif()

# OpenMP 3.0 is required for the task construct
find_package(OpenMP 3.0 REQUIRED) 

add_executable(hw main.cpp my_shit/fib.cpp)

target_link_libraries(hw PUBLIC OpenMP::OpenMP_CXX)