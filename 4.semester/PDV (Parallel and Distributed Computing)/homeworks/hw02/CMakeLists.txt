cmake_minimum_required(VERSION 3.15)
project(hw)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_BUILD_TYPE "Release")

if(MSVC)
	add_compile_options("/W4")
	add_compile_options("/O2")
else()
	add_compile_options("-Wall" "-Wextra")
	add_compile_options("-O3")
endif()

find_package(OpenMP REQUIRED)

add_executable(hw.bin main.cpp _dataGenerator/DataGenerator.cpp SumsOfVectors.cpp SumsOfVectors.h _executor/Executor.cpp _executor/Executor.h _outputGenerator/TextTable.h)

target_link_libraries(hw.bin PUBLIC OpenMP::OpenMP_CXX)