cmake_minimum_required(VERSION 3.15)
project(PDV_Search)
#set(CMAKE_CXX_COMPILER "\\wsl$\\Ubuntu-20.04\\usr\\lib\\llvm-10\\bin\\clang++")
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_BUILD_TYPE "Release")

if(MSVC)
	add_compile_options("/W4")
	add_compile_options("/O2")
else()
	add_compile_options("-Wall" "-Wextra")
	add_compile_options("-O3")
	add_compile_options("-march=native")
endif()

find_package(OpenMP REQUIRED)

add_executable(search main.cpp state.h domains/hanoi.h domains/maze.h domains/sat.h domains/slidingPuzzle.h
        domains/utils.h algorithms/bfs.cpp algorithms/bfs.h algorithms/iddfs.cpp algorithms/iddfs.h "domains/hanoi.h" "domains/maze.h" "domains/sat.h" "domains/slidingPuzzle.h" "algorithms/not_ working_shit.cpp")

target_link_libraries(search PUBLIC OpenMP::OpenMP_CXX)