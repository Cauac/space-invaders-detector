(ns space-invaders-detector.core
  (:require [space-invaders-detector.matrix :as matrix]))

(def default-detection-threshold 75)

(defn read-invader-samples
  "Reads collection of detection samples from the provided file.
   A typical sample is multiline string with `-` or `o` characters.
   All other symbols are skipped and used to divide samples."
  [file]
  (->> (slurp file)
       (re-seq #"([-o]+\n?)+")
       (map first)
       (map matrix/str->matrix)))

(defn read-radar-sample
  "Reads radar input from the provided file."
  [file]
  (-> (slurp file)
      (matrix/str->matrix)))

(defn check-area [data pattern coordinates]
  (let [checked-area (matrix/crop data coordinates)
        score (matrix/rate-match checked-area pattern)]
    {:score score
     :location coordinates
     :area checked-area}))

(defn scan [data pattern min-accuracy]
  (->> (matrix/find-possible-locations data pattern)
       (pmap (partial check-area data pattern))
       (filter #(< min-accuracy (:score %)))))

(defn run []
  (let [invaders (read-invader-samples "resources/invader-samples.txt")
        radar-data (read-radar-sample "resources/radar-sample.txt")
        results (mapcat #(scan radar-data % default-detection-threshold) invaders)]
    (if (seq results)
      (do
        (println "Detected hostile objects:")
        (doseq [{:keys [score location area]} results]
          (println "Location:" location)
          (println "Accuracy:" score "%")
          (matrix/print-matrix area))
        (println ""))
      (println "No enemies are found"))))