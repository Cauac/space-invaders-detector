(ns space-invaders-detector.matrix-test
  (:require [clojure.test :refer :all]
            [space-invaders-detector.matrix :refer :all]))

(def matrix-1x1 [[0]])
(def matrix-2x2 [[0 0]
                 [1 1]])
(def matrix-4x3 [[0 0 0]
                 [1 1 1]
                 [2 2 2]
                 [3 3 3]])
(def matrix-4x1 [[0]
                 [1]
                 [2]
                 [3]])

(deftest str->matrix-test
  (testing "Basic test"
    (let [input "123\n456"
          expected [[\1 \2 \3]
                    [\4 \5 \6]]]
      (is (= expected (str->matrix input)))))
  (testing "Empty string input"
    (is (= nil (str->matrix ""))))
  (testing "Blank string input"
    (is (= [[\space \space]] (str->matrix "  "))))
  (testing "String with special characters"
    (let [input " #\t\n ^$"
          expected [[\space \# \tab]
                    [\space \^ \$]]]
      (is (= expected (str->matrix input))))))

(deftest matrix-height-test
  (testing "Basic test"
    (is (= 1 (height matrix-1x1)))
    (is (= 2 (height matrix-2x2)))
    (is (= 4 (height matrix-4x3)))
    (is (= 4 (height matrix-4x1))))
  (testing "Empty matrix"
    (is (= 0 (height nil)))))

(deftest matrix-width-test
  (testing "Basic tests"
    (is (= 1 (width matrix-1x1)))
    (is (= 2 (width matrix-2x2)))
    (is (= 3 (width matrix-4x3)))
    (is (= 1 (width matrix-4x1))))
  (testing "Empty matrix"
    (is (= 0 (width nil)))))

(deftest matrix-size-test
  (testing "Basic test"
    (is (= 1 (size matrix-1x1)))
    (is (= 4 (size matrix-2x2)))
    (is (= 12 (size matrix-4x3)))
    (is (= 4 (size matrix-4x1))))
  (testing "Empty matrix"
    (is (= 0 (size nil)))))

(deftest find-possible-locations-test
  (testing "Basic tests with height and width"
    (is (= [[0 0 3 2]] (find-possible-locations matrix-4x3 4 3)))
    (is (= [[0 0 3 1] [0 1 3 2]] (find-possible-locations matrix-4x3 4 2)))
    (is (= [[0 0 3 0] [0 1 3 1] [0 2 3 2]] (find-possible-locations matrix-4x3 4 1)))
    (is (= [[0 0 2 1] [0 1 2 2] [1 0 3 1] [1 1 3 2]] (find-possible-locations matrix-4x3 3 2)))
    (is (= [[0 0 1 1] [0 1 1 2] [1 0 2 1] [1 1 2 2] [2 0 3 1] [2 1 3 2]] (find-possible-locations matrix-4x3 2 2))))
  (testing "Basic tests with another matrix"
    (is (= [[0 0 3 2]] (find-possible-locations matrix-4x3 matrix-4x3)))
    (is (= [[0 0 3 0] [0 1 3 1] [0 2 3 2]] (find-possible-locations matrix-4x3 matrix-4x1)))
    (is (= [[0 0 1 1] [0 1 1 2] [1 0 2 1] [1 1 2 2] [2 0 3 1] [2 1 3 2]] (find-possible-locations matrix-4x3 matrix-2x2)))
    (is (= [[0 0 0 0] [0 1 0 1] [1 0 1 0] [1 1 1 1]] (find-possible-locations matrix-2x2 matrix-1x1))))
  (testing "Testing against bigger matrix. Expected no possible locations."
    (is (= [] (find-possible-locations matrix-1x1 matrix-2x2)))
    (is (= [] (find-possible-locations matrix-1x1 matrix-4x3)))
    (is (= [] (find-possible-locations matrix-4x1 matrix-4x3))))
  (testing "One or both matrices are empty"
    (is (= [] (find-possible-locations [] 2 2)))
    (is (= nil (find-possible-locations [] 0 0)))
    (is (= nil (find-possible-locations [] [])))
    (is (= nil (find-possible-locations [] nil)))
    (is (= nil (find-possible-locations nil nil)))
    (is (= nil (find-possible-locations matrix-4x3 0 0)))))

(deftest crop-test
  (testing "Basic tests"
    (is (= [[0]] (crop matrix-2x2 [0 0 0 0])))
    (is (= [[1 1]] (crop matrix-2x2 [1 0 1 1])))
    (is (= matrix-4x1 (crop matrix-4x3 [0 0 3 0])))
    (is (= [[1 1] [2 2]] (crop matrix-4x3 [1 1 2 2])))))

(deftest crop-integration-test
  (testing "Integration possible location search and crop function"
    (testing "For 2x2 matrix and 2x1 search pattern it should return two cut options"
      (let [matrix [["11" "12"]
                    ["21" "22"]]
            search-matrix [["*"]
                           ["*"]]
            expected-cut-1 [["11"]
                            ["21"]]
            expected-cut-2 [["12"]
                            ["22"]]
            results (->> (find-possible-locations matrix search-matrix)
                         (map #(crop matrix %)))]
        (is (= [expected-cut-1 expected-cut-2] results))))))

(deftest rate-match-test
  (testing "Checking match of matrices. The more equal element the bigger the score."
    (let [m-1 [[\# \#] [\@ \@]]
          m-2 [[\* \*] [\* \*]]]
      (is (= 0.0 (rate-match m-1 m-2))))
    (let [m-1 [[\# \#] [\@ \@]]
          m-2 [[\@ \@] [\@ \@]]]
     (is (= 50.0 (rate-match m-1 m-2))))
    (let [m-1 [[\# \#] [\@ \@]]
          m-2 [[\@ \#] [\@ \@]]]
      (is (= 75.0 (rate-match m-1 m-2))))
    (let [m-1 [[\# \#] [\@ \@]]
          m-2 [[\# \#] [\@ \@]]]
      (is (= 100.0 (rate-match m-1 m-2))))
    (let [m-1 [[\# \#] [\@ \@]]
          m-2 [[\# \#] [\@ \@]]]
      (is (= 100.0 (rate-match m-1 m-2))))

    (testing "Unequal size matrices compared by the smallest one."
     (is (= 100.0 (rate-match matrix-4x1 matrix-4x3)))
     (let [m-1 [[\# \*]]
           m-2 [[\# \#] [\@ \@]]]
       (is (= 50.0 (rate-match m-1 m-2)))))))

(deftest update-indexed-test
  (testing "Basic test"
    (is (= [["000"]] (update-indexed matrix-1x1 str)))
    (is (= [["000" "010"] ["101" "111"]] (update-indexed matrix-2x2 str)))
    (is (= [["000"] ["101"] ["202"] ["303"]] (update-indexed matrix-4x1 str)))))