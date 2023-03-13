# EAR Seminar 10 Tasks (Winter Term 2022)

## Task 1: (1 point)
Implement `DefaultAuthenticationProvider.authenticate` method to return the expected authentication information.

Acceptance criteria: Tests in `DefaultAuthenticationProviderTest` should pass.

Hints: Use `SecurityUtils`, `UserDetails`, and `UserDetailsService` classes in the method implementation.

## Task 2: (1 point)
1. Data modifying operations in `CategoryController` should be allowed only for administrators. Use appropriate Spring security
   features to secure the corresponding endpoints.

2. Method `OrderService.findAll` should return only the orders of the current user or all orders if the current user is administrator.

Acceptance criteria:

1. All tests in `CategoryControllerSecurityTest` pass.

2. All tests in `OrderServiceSecurityTest` pass.

Hints: Administrators are users with `Role.ADMIN`.

### License
LGPLv3
