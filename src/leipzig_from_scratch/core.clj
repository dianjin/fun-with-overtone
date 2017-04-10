(ns leipzig-from-scratch.core
  (:require [overtone.live :as overtone]
            [overtone.inst.drum :refer :all]
            [overtone.inst.synth :refer :all]
            [leipzig.melody :refer [all bpm is phrase tempo then times where with]]
            [leipzig.scale :as scale]
            [leipzig.live :as live]))

(overtone/definst bassline [freq 200 decay 0.1 vol 3]
  (* (overtone/env-gen (overtone/perc 0.05 0.1) :action overtone/FREE)
     (overtone/lf-tri freq)
     vol))

(overtone/definst saw-synth [freq 220 dur 1.0 vol 1]
  (* (overtone/env-gen  (overtone/perc 0.05 0.4) :action overtone/FREE)
     (overtone/saw freq)
     ))

(def bass-1
  (->>
   (phrase
    [1/2 1/2 1/2 1/2 1/2 1/2 1/2 1/2]
    [-5  3   -5  3   -5  3   -5  3  ])
   (all :part :bass)))

(def bass-2
  (->>
   (phrase
    [1/2 1/2 1/2 1/2 1/2 1/2 1/2 1/2]
    [-6  2   -6  2   -6  2   -6  2  ])
   (all :part :bass)))

(defmethod live/play-note :bass [{midi :pitch}]
  (-> midi overtone/midi->hz (/ 4) (bassline 0.5)))

(defmethod live/play-note :default [{midi :pitch dur :duration}]
  (some-> midi overtone/midi->hz (saw-synth midi 0.4)))

(def melody-A1
  (phrase
   [1/2]
   [0])) 

(def melody-A2
  (phrase
   [7/2 1/2]
   [nil -1])) 

(def better-off-alone-p1
  (->>
   (with bass-1 melody-A1) 
   (then (with bass-2 melody-A2))
   (then (with bass-1 melody-A1))
   (then bass-2)
   )
  )

(def melody-B1
  (phrase
   [1 1/2 1/2]
   [0 0   -2]))

(def melody-B2
  (phrase
   [2 3/4 3/4 1/2]
   [nil 4 4 2]))

(def melody-B3
  (phrase
   [1 1/2 1/2]
   [0 0 -2]))

(def melody-B4
  (phrase
   [2 3/4 3/4 1/2]
   [nil 3 3 2]))

(def better-off-alone-p2
  (->>
   (with bass-1 melody-B1)
   (then (with bass-2 melody-B2))
   (then (with bass-1 melody-B3))
   (then (with bass-2 melody-B4))
   (then (with bass-1 melody-B1))
   (then (with bass-2 melody-B2))
   (then (with bass-1 melody-B3))
   (then (with bass-2 melody-B4))
   )
  )

(def melody-C1
  (phrase
   [1   1/2  1  1 1/2]
   [0   0   -2  0   0]))

(def melody-C2
  (phrase
   [1/2 1   1/2 3/4 3/4 1/2]
   [nil -1  -3  4   4   2  ])
   )

(def melody-C3
  (phrase
   [1/2 1   1/2 3/4 3/4 1/2]
   [nil -1  -3  3   3   2  ])
   )
  
(overtone/definst sin-synth [freq 220 dur 1.0 vol 1]
  (* (overtone/env-gen  (overtone/perc 0.05 1) :action overtone/FREE)
     (overtone/sin-osc freq)
     ))

(def bass-1
  (->>
   (phrase
    [1/2 1/2 1/2 1/2 1/2 1/2 1/2 1/2]
    [-5  3   -5  3   -5  3   -5  3  ])
   (all :part :bass)))

(def better-off-alone-p3
  (->>
   melody-C1
   (then melody-C2)
   (then melody-C1)
   (then melody-C3)
   ))

(def better-off-alone
  (->>
   ; Part 1
   ; (times 2 better-off-alone-p1)
   ; Part 2
   ; (then (times 2 better-off-alone-p2))
   better-off-alone-p3
   (tempo (bpm 138))
   (where :pitch (comp scale/B scale/major))))


