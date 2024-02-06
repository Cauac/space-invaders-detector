(ns space-invaders-detector.detector
  (:require [space-invaders-detector.matrix :as matrix]
            [space-invaders-detector.target :as target]
            [clojure.term.colors :as colors]))

(defn- check-area
  "Focuses on a specific area using provided coordinates
   and tries to locate invader within selected area."
  [data pattern coordinates]
  (let [checked-area (matrix/crop data coordinates)
        accuracy (matrix/rate-match checked-area pattern)]
    (target/->LocatedInvader coordinates checked-area accuracy)))

(defn scan
  "Scans the radar picture and returns targets that meet accuracy specs."
  [data pattern min-accuracy]
  (->> (matrix/find-possible-locations data pattern)
       (pmap (partial check-area data pattern))
       (filter #(target/accurate? % min-accuracy))))

(defn highlight-targets
  "Marks located targets on the radar picture."
  [data targets]
  (matrix/update-indexed
    data
    (fn [y x element]
      (if (some #(target/includes-point? % y x) targets)
        (colors/red element)
        element))))