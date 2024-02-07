(ns space-invaders-detector.detector-test
  (:require [clojure.test :refer :all]
            [space-invaders-detector.matrix-test :refer [matrix-1x1 matrix-2x2 matrix-4x3]]
            [space-invaders-detector.detector :refer :all]
            [space-invaders-detector.target :as target]
            [clojure.term.colors :as colors]))

(deftest check-area-test
  (let [f #'space-invaders-detector.detector/check-area]
    (let [{:keys [location data accuracy]} (f matrix-1x1 [["*"]] [0 0 0 0])]
      (is (= 0.0 accuracy))
      (is (= [0 0 0 0] location))
      (is (= [[0]] data)))
    (let [{:keys [location data accuracy]} (f matrix-1x1 [[0]] [0 0 0 0])]
      (is (= 100.0 accuracy))
      (is (= [0 0 0 0] location))
      (is (= [[0]] data)))
    (let [{:keys [location data accuracy]} (f matrix-2x2 [[0 0] [1 0]] [0 0 1 1])]
      (is (= 75.0 accuracy))
      (is (= [0 0 1 1] location))
      (is (= matrix-2x2 data)))
    (let [{:keys [location data accuracy]} (f matrix-2x2 [[0] [2]] [0 1 1 1])]
      (is (= 50.0 accuracy))
      (is (= [0 1 1 1] location))
      (is (= [[0] [1]] data)))))

(deftest scan-test
  (let [results (scan matrix-1x1 [["*"]] 50)]
    (is (= [] results)))
  (let [results (scan matrix-1x1 [[0]] 50)
        expected (target/->LocatedInvader [0 0 0 0] [[0]] 100.0)]
    (is (= 1 (count results)))
    (is (= expected (first results))))
  (let [results (scan matrix-4x3 [[1 "*"] [2 2]] 50)
        expected-1 (target/->LocatedInvader [1 0 2 1] [[1 1] [2 2]] 75.0)
        expected-2 (target/->LocatedInvader [1 1 2 2] [[1 1] [2 2]] 75.0)]
    (is (= 2 (count results)))
    (is (= [expected-1 expected-2] results)))
  (let [data [[1 0 0]
              [1 1 1]
              [2 2 2]]
        pattern matrix-2x2
        results (scan data pattern 50)
        expected-1 (target/->LocatedInvader [0 0 1 1] [[1 0] [1 1]] 75.0)
        expected-2 (target/->LocatedInvader [0 1 1 2] [[0 0] [1 1]] 100.0)]
    (is (= 2 (count results)))
    (is (= [expected-1 expected-2] results))))

(deftest highlight-targets-test
  (let [data [[1 2 3]
              [4 5 6]
              [7 8 9]]
        targets [(target/->LocatedInvader [0 2 1 2] [[3] [6]] 100.0)
                 (target/->LocatedInvader [1 0 2 0] [[4] [7]] 100.0)]
        expected [[1              2 (colors/red 3)]
                  [(colors/red 4) 5 (colors/red 6)]
                  [(colors/red 7) 8 9]]]
    (is (= expected (highlight-targets data targets)))))