(ns space-invaders-detector.matrix
  (:require [clojure.string :as str]))

(defn str->matrix
  "Converts text representation of matrix into 2-dimensional array of symbols.
  Example:
   '-o-     [[- o -]
    ooo  =>  [o o o]
    -o-'     [- o -]]"
  [^CharSequence s]
  (when-not (empty? s)
    (->> (str/split-lines s)
         (mapv vec))))

(defn height [matrix]
  (count matrix))

(defn width [matrix]
  (count (first matrix)))

(defn size [matrix]
  (* (height matrix) (width matrix)))

(defn find-possible-locations
  "For given matrix finds all possible locations where another matrix can fit.
   Returns list of locations, each element is [Y1, X1, Y2, X2] coordinates
   Examples:
    By providing another matrix size parameters
     [[ 1 2 ]  , h=1, w=2 => [[0 0 0 1] [1 0 1 1]]
      [ 3 4 ]]
    By providing another matrix
     [[ 1 2 ]  , [ * * ] => [[0 0 0 1] [1 0 1 1]]
      [ 3 4 ]]
   "
  ([matrix another-matrix]
   (find-possible-locations matrix (height another-matrix) (width another-matrix)))
  ([matrix m-height m-width]
   (when (and (pos? m-height) (pos? m-width))
     (let [matrix-height (height matrix)
           matrix-width (width matrix)]
       (for [y1 (range (- matrix-height m-height -1))
             x1 (range (- matrix-width m-width -1))
             :let [y2 (+ y1 m-height -1)
                   x2 (+ x1 m-width -1)]]
         [y1 x1 y2 x2])))))

(defn crop
  "Returns sub-matrix defined by its top-left and bottom-right coordinates."
  [matrix [y1 x1 y2 x2]]
  (->> (subvec matrix y1 (inc y2))
       (mapv #(subvec % x1 (inc x2)))))

(defn- match-elements [e1 e2]
  (if (= e1 e2) 1 0))

(defn- match-vectors [v1 v2]
  (reduce + (map match-elements v1 v2)))

(defn rate-match
  "Calculates match of two matrices in percentages"
  [matrix another-matrix]
  (let [equal-elements (reduce + (map match-vectors matrix another-matrix))
        size (size matrix)]
    (/ (* 100.0 equal-elements) size)))

(defn print-matrix [matrix]
  (doseq [vector matrix]
    (println (apply str vector))))

(defn update-indexed
  "Walks through the matrix and updates each element with provided function.
   Thus function f should accept 3 arguments (y,x coordinates and element value)."
  [matrix f]
  (vec
    (map-indexed
      (fn [y v]
        (vec (map-indexed (fn [x el] (f y x el)) v)))
      matrix)))
