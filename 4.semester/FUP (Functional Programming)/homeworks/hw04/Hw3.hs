module Hw3 where

-- Program is inspired by 
-- https://cw.fel.cvut.cz/wiki/courses/fup/tutorials/lab_7_-_lambda_calculus

type Symbol = String

data Expr
  = Var Symbol
  | App Expr Expr
  | Lambda Symbol Expr
  deriving (Eq)

symbols :: [Symbol]
symbols = ["a" ++ show x | x <- [0 ..]]

neededSymbol :: Int -> Symbol
neededSymbol n = symbols !! n

instance Show Expr where
  show (Var x)           = x
  show (App left right)  = "(" ++ show left ++ " " ++ show right ++ ")"
  show (Lambda sym body) = "(\\" ++ sym ++ "." ++ show body ++ ")"

removeItem :: Symbol -> [Symbol] -> [Symbol]
removeItem x [] = []
removeItem x (y:ys)
  | x == y = removeItem x ys
  | otherwise = y : removeItem x ys

addToSet :: Symbol -> [Symbol] -> [Symbol]
addToSet x [] = [x]
addToSet x [y]
  | x == y = [y]
  | otherwise = [y, x]
addToSet x (y:ys)
  | x == y = y : ys
  | otherwise = addToSet x ys

getFreeVars :: Expr -> [Symbol] -> [Symbol]
getFreeVars (Var x) acc = addToSet x acc
getFreeVars (App left right) acc = getFreeVars left acc ++ getFreeVars right acc
getFreeVars (Lambda sym body) acc = removeItem sym (getFreeVars body acc)

checkVar :: Expr -> Int -> Symbol -> Expr -> Expr
checkVar (Lambda sym body) n old new
  | sym `elem` getFreeVars new [] =
    Lambda
      (neededSymbol n)
      (substitute
         (substitute body sym (Var (neededSymbol n)) (n + 1))
         old
         new
         (n + 1))
  | otherwise = Lambda sym (substitute body old new n)

substitute :: Expr -> Symbol -> Expr -> Int -> Expr
substitute (Var expr) old new n
  | expr == old = new
  | otherwise = Var expr
substitute (Lambda sym body) old new n
  | old == sym = Lambda sym body
  | otherwise = checkVar (Lambda sym body) n old new
substitute (App left right) old new n =
  App (substitute left old new n) (substitute right old new n)

-- Implementation of call by name function is inspired by
-- https://www.cs.cornell.edu/courses/cs6110/2014sp/Handouts/Sestoft.pdf
callByName :: Expr -> Int -> Expr
callByName (Var x) n = Var x
callByName (Lambda sym body) n = Lambda sym body
callByName (App left right) n =
  case callByName left n of
    (Lambda sym body) -> callByName (substitute body sym right n) n
    e_1 -> App e_1 right

reduce :: Expr -> Int -> Expr
reduce (Var x) n = Var x
reduce (Lambda sym body) n = Lambda sym (reduce body n)
reduce (App left right) n =
  case callByName left n of
    (Lambda sym body) -> reduce (substitute body sym right n) n
    e_1 ->
      let e__1 = reduce e_1 n
       in App e__1 (reduce right n)

      
eval :: Expr -> Expr
eval lam =
  if new_expr == lam
    then new_expr
    else reduce new_expr 1
  where
    new_expr = reduce lam 0
