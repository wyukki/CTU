#lang racket
(require racket/trace)
(provide execute)

; Assignment description is in hw2.pdf file
; My solution doesn't work. I've had no time to reimplement it.

(define (execute width height prg expr)
  (define functions_list (match_func prg))
  (string-append (string-append (get_header width height) (eval_exp expr functions_list)) "</svg>")
  )


(define (eval_num_op exp)
  (match exp
    [(? string? s) s]
    [(? number? x) x]
    [(list '+ args ...) (apply + (map eval_num_op args))]
    [(list '- args ...) (apply - (map eval_num_op args))]
    [(list '* args ...) (apply * (map eval_num_op args))]
    [(list '/ args ...) (apply / (map eval_num_op args))]
    [(list 'floor args ...) (apply floor (map eval_num_op args))]
    [(list 'cos args ...) (apply cos (map eval_num_op args))]
    [(list 'sin args ...) (apply sin (map eval_num_op args))]
    )
  )

(define (eval_bool_op exp)
  (match exp
    [(? boolean? b) b]
    [(list '= args ...) (apply equal? (map eval_num_op args))]
    [(list '< args ...) (apply < (map eval_num_op args))]
    [(list '> args ...) (apply > (map eval_num_op args))]
    )
  )

(define (eval_if cond true false)
  (if  (eval_bool_op  cond)
       true
       false
       )
  )

(define (eval_when cond exps)
  (if (eval_bool_op cond)
      exps
      ""
      )
  )
(define (get_header width height)
  (format "<svg width=\"~a\" height=\"~a\">" width height)
  )

(define (get_circle x y r style)
  (format "<circle cx=\"~a\" cy=\"~a\" r=\"~a\" style=\"~a\"/>"
          (eval_num_op x) (eval_num_op y) (eval_num_op r) (eval_num_op style))
  )

(define (get_rect x y w h style)
  (format "<rect x=\"~a\" y=\"~a\" width=\"~a\" height=\"~a\" style=\"~a\"/>"
          (eval_num_op x) (eval_num_op y) (eval_num_op w)(eval_num_op  h) (eval_num_op style))
  )
(define (get_line x1 y1 x2 y2 style)
  (format "<line x1=\"~a\" y1=\"~a\" x2=\"~a\" y2=\"~a\" style=\"~a\"/>"
          (eval_num_op x1) (eval_num_op y1) (eval_num_op x2) (eval_num_op y2) (eval_num_op style))
  )

(define (get_str func)
  (match func
    [(list 'line x1 y1 x2 y2 style) (get_line x1 y1 x2 y2 style)]
    [(list 'circle x y r style) (get_circle x y r style)]
    [(list 'rect x y w h style) (get_rect x y w h style)]
    )
  )
(define (recur exp functions_list [str ""])
  (match (car exp)
    [(list 'if cond true false) (lang_eval (eval_if cond true false) functions_list)]
    [(list 'when cond exps ...) (lang_eval (eval_when cond exps) functions_list)]
    [(list 'line x1 y1 x2 y2 style) (recur (cdr exp) functions_list (string-append (get_str (car exp)) str))]
    [(list 'circle x y r style) (recur (cdr exp) functions_list (string-append (get_str (car exp)) str))]
    [(list 'rect x y w h style) (recur (cdr exp) functions_list (string-append (get_str (car exp)) str))]
    [_ (eval_exp exp functions_list)]
    )
  )
(trace get_str)
(define (lang_eval exp functions_list)
  (match exp
    ["" ""]
    [(list 'if cond true false) (lang_eval (eval_if cond true false) functions_list)]
    [(list 'when cond exps ...) (lang_eval (eval_when cond exps) functions_list)]
    [(list singleton ...) (recur singleton functions_list)]
    )
  )

(define (get_arg_values exp args [vals '()] [arg_num 1])
  (if (null? args) vals
      (get_arg_values exp (cdr args) (cons (list (car args) (list-ref exp arg_num)) vals) (+ arg_num 1))
      )
  )

(define (get_arg_val arg values)
  (define key-val (assv arg values))
  (if (list? key-val) (car (cdr key-val))
      arg)
  )

(define (change_occurrence exp values)
  (cond
    ([null? exp] '())
    ([list? exp]
     (let* ((c (car exp))
            (nc (cond ([list? c] (change_occurrence c values))
                      (else (get_arg_val c values)))))
       (cons nc (change_occurrence (cdr exp) values))))
     (else exp)))


(define (eval_func exp func args functions_list [str ""])
  (define func_body (car func))
  (cond
    ([null? args] (if (or (null? func_body) (null? func)) str
                       (eval_func exp (list (cdr (car func))) args functions_list(string-append str (lang_eval (car func_body) functions_list)))))
    (else
      (define vals (get_arg_values exp args))
      (define changed (change_occurrence func_body vals))
      (if (or (null? func_body) (null? func)) str
          (eval_func exp (list (cdr (car func))) args functions_list (string-append str (lang_eval changed functions_list)))
          )
      )
    )
  )

(define (eval_exp exp functions_list)
  (define curr_func (find_func (car exp) functions_list))
  (if (list? curr_func) (eval_func exp (list-ref curr_func 2) (list-ref curr_func 1) functions_list)
      (lang_eval exp functions_list)
      )
  )

(define (create_dict func body)
  (if (= (length body ) 1) (list func '() body) ; <- function without arguments 
      (list func (car body) (cdr body))) ; <- function with arguments
  )

(define (match_func prg [functions '()])
  (if (null? prg) functions                                                                                                                                                 
      (match (car prg)
        [(list 'define (list func_name) body ...) (match_func (cdr prg) (cons (create_dict func_name (list body)) functions))]    ;<- function without arguments 
        [(list 'define (list func_name args ...) body ...) (match_func (cdr prg) (cons (create_dict func_name (cons args body)) functions))] ; <- function with arguments
        )
      )
  )
      
(define (find_func func_name my_list)
  (assv func_name my_list))
(define test2
  '((define (circles x r)
      (when (> r 10)
        (circle x 200 r "fill:white;stroke:green")
        (circles (+ x (floor (/ r 2.0))) (floor (/ r 2)))
        )
      )
    )
  )
(display (execute 400 400 test2 '(circles 200 200)))

