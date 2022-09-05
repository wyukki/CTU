module Hw4 where

import           Control.Applicative
import           Data.Char
import           Hw3
import           Parser

-- Assignment is in hw4.pdf file

parseExpr :: Parser Expr
parseExpr = parseVar <|> parseApp <|> parseLambda 

parseVar :: Parser Expr
parseVar = do
  x <- some alphaNum
  return (Var x)

parseApp :: Parser Expr
parseApp = do
  char '('
  x <- parseExpr
  sep
  y <- parseExpr
  char ')'
  return (App x y)

parseLambda :: Parser Expr
parseLambda = do
  string "(\\"
  x <- some alphaNum
  char '.'
  y <- parseExpr
  char ')'
  return (Lambda x y)

readPrg :: String -> Maybe Expr
readPrg str =
  case parse parseLambda str of
    Just (expr, str) -> Just expr
    _                -> Nothing
