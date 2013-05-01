


(define (problem mixed-f5-p2-u20-v5-d5-a60-n10-A20-B80-N50-F5)
   (:domain miconic)
   (:objects p0 p1 - passenger
             f0 f1 f2 f3 f4 - floor)


(:init
(above f0 f1)
(above f0 f2)
(above f0 f3)
(above f0 f4)

(above f1 f2)
(above f1 f3)
(above f1 f4)

(above f2 f3)
(above f2 f4)

(above f3 f4)



(origin p0 f4)
(destin p0 f2)

(origin p1 f3)
(destin p1 f1)






(lift-at f0)
)


(:goal (forall (?p - passenger) (served ?p)))
)


