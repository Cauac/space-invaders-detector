(ns space-invaders-detector.core
  (:require
    [space-invaders-detector.detector :as detector]
    [space-invaders-detector.matrix :as matrix]))

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

(defn run []
  (let [invaders (read-invader-samples "resources/invader-samples.txt")
        radar-data (read-radar-sample "resources/radar-sample.txt")
        targets (mapcat #(detector/scan radar-data % default-detection-threshold) invaders)]
    (if (seq targets)
      (do
        (println "Detected hostile objects:")
        (doseq [{:keys [accuracy location data]} targets]
          (println "Location:" location)
          (println "Accuracy:" accuracy "%")
          (matrix/print-matrix data))
        (println "Processed radar picture:")
        (-> (detector/highlight-targets radar-data targets)
            (matrix/print-matrix)))
      (println "No enemies are found"))))