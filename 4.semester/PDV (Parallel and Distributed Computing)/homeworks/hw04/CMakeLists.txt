cmake_minimum_required(VERSION 3.15)
project(DatabaseQueries)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_BUILD_TYPE "Release")
set(OMP_CANCELLATION=true)
if(MSVC)
	add_compile_options("/W4")
	add_compile_options("/O2")
else()
	add_compile_options("-Wall" "-Wextra")
	add_compile_options("-O3")
endif()

find_package(OpenMP 4.0 REQUIRED) 

add_executable(DatabaseQueries main.cpp query.h _generator/generator.cpp)

target_link_libraries(DatabaseQueries PUBLIC OpenMP::OpenMP_CXX)