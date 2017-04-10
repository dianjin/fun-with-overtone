(ns leipzig-from-scratch.windows
  (:require [overtone.live :as overtone]
            [overtone.inst.drum :refer :all]
            [overtone.inst.synth :refer :all]
            [leipzig.melody :refer [all bpm is phrase tempo then times where with]]
            [leipzig.scale :as scale]
            [leipzig.live :as live]))

(def error
  (overtone/sample "windows_xp_exclamation.wav"))

(def startup
  (overtone/sample "windows_xp_startup.wav"))

(def drum-kit
  {:hi-hat open-hat
   :close (partial snare 300 2)})

(defmethod live/play-note :beat [note]
  (let [func (get drum-kit (:drum note))]
    (func)))

(defmethod live/play-note :bass [_] (error))

(defmethod live/play-note :melody [_] (startup))

(defn windows-melody [times]
  (map #(zipmap [:time :duration] [%1 8]) times))

(defn windows-bass [times]
  (map #(zipmap [:time :duration] [%1 1]) times))

(defn tap [drum times]
  (map #(zipmap [:drum :time :duration]
                [drum %1 1]) times))

(def bassline
  (->>
   (windows-bass (range 0 8 2))
   (all :part :bass)))

(def beats
  (->>
   (with (tap :hi-hat (range 0 8 2)) (tap :close (range 1 8 2)))
   (all :part :beat)
   ))

(def melody
  (->>
   (windows-melody [0])
   (all :part :melody)))

(def windows
  (->> bassline
       (then (with bassline beats))
       ;(then (with bassline beats melody))
       (tempo (bpm 180))))
