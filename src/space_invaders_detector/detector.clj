(ns space-invaders-detector.detector
  (:require [space-invaders-detector.matrix :as matrix]
            [space-invaders-detector.target :as target]
            [clojure.term.colors :as colors]
            [clojure.math :as math]))

(defn- check-area
  "Focuses on a specific area using provided coordinates
   and tries to locate invader within selected area."
  [data pattern coordinates]
  (let [checked-area (matrix/crop data coordinates)
        accuracy (matrix/rate-match checked-area pattern)]
    (target/->LocatedInvader coordinates checked-area accuracy)))

(defn- calculate-frame-width
  "Defines how far can the pattern be out of the picture
   and still be detected with provided accuracy.
   E.g for accuracy 75% and matrix 4x3, 25% of the picture can be hidden."
  [pattern accuracy]
  (-> (max (matrix/height pattern) (matrix/width pattern))
      (/ 100)
      (* (- 100 accuracy))
      (math/round)))

(defn scan
  "Scans the radar picture and returns targets that meet accuracy specs."
  [data pattern min-accuracy]
  (let [frame-width (calculate-frame-width pattern min-accuracy)
        framed-data (matrix/frame data frame-width)]
    (->> (matrix/find-possible-locations framed-data pattern)
         (pmap (partial check-area framed-data pattern))
         (filter #(target/accurate? % min-accuracy))
         (map #(target/translation-location % frame-width)))))

(defn highlight-targets
  "Marks located targets on the radar picture."
  [data targets]
  (matrix/update-indexed
    data
    (fn [y x element]
      (if (some #(target/includes-point? % y x) targets)
        (colors/red element)
        element))))