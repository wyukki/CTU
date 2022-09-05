#lang racket
(require 2htdp/image)
(require racket/trace)
(provide img->mat
         ascii-art)
; Assignment description is in "hw1.pdf" file
(define (RGB->grayscale color)
  (+ (* 0.3 (color-red color))
     (* 0.59 (color-green color))
     (* 0.11 (color-blue color))
  ))

(define (convert_to_matrix grayscale_list img_width img_height [acc 0] [rows '()][row '()])
  (cond
    ([= acc (* img_width img_height)] (reverse (cons (reverse row) rows)))
    ([and (= (modulo acc img_width) 0) (not (= acc 0))] (convert_to_matrix (cdr grayscale_list) img_width img_height (+ acc 1) (cons (reverse row) rows) (list (car grayscale_list))))
    (else (convert_to_matrix (cdr grayscale_list) img_width img_height (+ acc 1) rows (cons (car grayscale_list) row)))))

(define (img->mat img)
  (if (null? img) '()
      (convert_to_matrix (map RGB->grayscale (image->color-list img)) (image-width img) (image-height img))
      )
  )

(define (get_cols_num row [cols 0])
  (if (null? row) cols
      (get_cols_num (cdr row) (+ 1 cols))
      )
  )

(define (get_rows_num mat [rows 0])
  (if (null? mat) rows
      (get_rows_num (cdr mat) (+ 1 rows))
      )
  )

(define (trim_row row cols_num [trimmed_row '()] [counter 0])
   (cond
     ([= counter cols_num] (reverse trimmed_row))
     (else
      (trim_row (cdr row) cols_num (cons (car row) trimmed_row) (+ counter 1))
      )
     )
  )

(define (trim_rows rows rows_num [trimmed_rows '()] [counter 0])
  (cond
    ([null? rows] (reverse trimmed_rows))
    ([= counter rows_num] (reverse trimmed_rows))
    (else
      (trim_rows (cdr rows) rows_num (cons (car rows) trimmed_rows) (+ counter 1))
      )
    )
  )

(define (trim_matrix mat cols_num [trimmed_mat '()])
  (cond
    ([null? mat] (reverse trimmed_mat))
    (else (trim_matrix (cdr mat) cols_num (cons (trim_row (car mat) cols_num) trimmed_mat))
          )
    )
  )

(define (find_end width list_length)
  (if (= (modulo list_length width) 0) list_length
      (find_end width (- list_length 1))
      )
  )

(define (sum_in_row lst width [summarized_lst '()] [sum 0] [counter 0])
  (cond
    ([null? lst] (reverse (cons sum summarized_lst)))
    ([= counter width] (sum_in_row lst width (cons sum summarized_lst) 0 0))
    (else (sum_in_row (cdr lst) width summarized_lst (+ (car lst) sum) (+ counter 1)))
    )
  )

(define (sum_in_rows mat width [summarized_rows '()])
  (if (null? mat) (reverse summarized_rows)
      (sum_in_rows (cdr mat) width (cons (sum_in_row (car mat) width) summarized_rows))
     )
  )

(define (find_avrg_row row koef [avrg_row '()])
  (if (null? row) (reverse avrg_row)
      (find_avrg_row (cdr row) koef (cons (/ (car row) koef) avrg_row))
      )
  )

(define (find_avrg mat koef [avrg_lst '()])
  (if (null? mat) (reverse avrg_lst)
      (find_avrg (cdr mat) koef (cons (find_avrg_row (car mat) koef) avrg_lst))
      )
  )

(define (transpose mat)   ;This function is taken from https://stackoverflow.com/questions/30775032/transpose-a-matrix-in-racket-list-of-lists
  (if (null? mat) '()
      (apply map list mat)
     )
  )

(define (my_print str)
  (display str)
  (display "\n")
  )

(define (compute_index intesity chars)
  (floor (/ (* (string-length chars) (- 255 (inexact->exact (floor intesity)))) 256)))

(define (get_char_by_intesity intesity chars)
  (cond
    ([= intesity 0] (string-ref chars (- (string-length chars) 1)))
    ([= intesity 255] (string-ref chars 0))
    (else
     (define index (compute_index intesity chars))
     (if (or (< index 0 ) (> index (string-length chars))) (my_print intesity)
         (string-ref chars index)
         )
     )
    )
  )

(define (sum_row->string row chars [string '()])
  (if
    (null? row) (string-append (list->string (reverse string)) "\n")
    (sum_row->string (cdr row) chars (cons (get_char_by_intesity (car row) chars) string))
    )
  )

(define (sum_mat->string mat chars [string ""])
  (cond
    ([null? mat] string)
    (else (sum_mat->string (cdr mat) chars (string-append string (sum_row->string (car mat) chars))))
    )
  )
(define (find_percent row chars)
  (cond
    ([null? row] -1)
    ([equal? (get_char_by_intesity (car row) chars) #\%] (display (car row)))
    (else (find_percent (cdr row) chars)
          )
    )
  )
                                             
(define (ascii-art width height chars)
  (if (or (= width 0) (= height 0 )) ""
      (lambda (image)
        (define mat (img->mat image))
        (define split_rows (trim_rows mat (find_end height  (get_rows_num mat))))
        (define split_mat (trim_matrix split_rows (find_end width (get_cols_num (car mat)))))
        (define summarized_mat (transpose (sum_in_rows (transpose (sum_in_rows split_mat width)) height)))
        (define avrg_mat (find_avrg summarized_mat (* width height)))
        (sum_mat->string avrg_mat chars)
        )
      )
  )
