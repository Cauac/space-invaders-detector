(ns space-invaders-detector.input-test
  (:require [clojure.test :refer :all]
            [space-invaders-detector.core :refer :all]))

(deftest read-invader-samples-test
  (testing "The same data formatted differently. Expected to be equal."
    (let [invaders-1 (read-invader-samples "test/resources/invaders-1.txt")
          invaders-2 (read-invader-samples "test/resources/invaders-2.txt")
          expected [[[\- \- \o \-]
                     [\- \o \o \-]
                     [\- \- \o \-]]
                    [[\o \- \o]
                     [\- \o \-]
                     [\- \o \-]]
                    [[\o \o \o \- \-]
                     [\o \o \o \- \-]
                     [\o \o \o \- \-]
                     [\- \- \- \- \-]]]]
      (is (= invaders-1 invaders-2))
      (is (= expected invaders-1)))))

(deftest read-radar-sample-test
  (let [radar-1 (read-radar-sample "test/resources/radar-1.txt")
        expected [[\- \o \- \- \- \- \- \- \- \- \- \- \o \-]
                  [\- \- \o \- \- \- \- \- \- \- \- \o \o \-]
                  [\- \- \- \o \- \- \- \- \- \- \- \- \o \-]
                  [\- \- \- \o \- \- \- \- \- \- \- \- \o \-]
                  [\- \- \o \- \- \- \- \- \- \- \- \- \- \-]
                  [\- \o \- \- \- \- \- \- \- \- \- \- \- \-]]]
    (is (= expected radar-1)))
  (let [radar-2 (read-radar-sample "test/resources/radar-2.txt")
        expected [[\- \o \- \- \- \- \- \- \- \- \- \- \o \-]
                  [\- \- \o \- \o \- \- \- \- \- \- \o \o \-]
                  [\- \- \- \o \- \- \- \- \- \- \- \- \o \-]
                  [\o \o \o \o \- \- \- \- \- \- \- \- \o \-]
                  [\o \o \o \- \- \- \- \- \o \- \o \- \- \-]
                  [\o \o \o \- \- \- \- \- \- \o \- \- \- \-]]]
    (is (= expected radar-2))))

(deftest process-input-params-test
  (testing "Console input processing. It shall always return nil when input is invalid or inconsistent."
    (testing "Valid params"
      (is (= {:patterns "file1" :data "file2" :threshold 98}
             (process-input-params ["--data" "file2" "--patterns" "file1" "--threshold" "98"]))))
    (testing "Invalid params scenario"
     (is (nil? (process-input-params [])))
     (is (nil? (process-input-params ["a" "1"])))
     (is (nil? (process-input-params ["--a" "1"])))
     (is (nil? (process-input-params ["b" "1" "b"])))
     (is (nil? (process-input-params ["--data" "file" "--patterns"])))
     (is (nil? (process-input-params ["--data" "file" "--patterns" "file" "--threshold"])))
     (is (nil? (process-input-params ["--data" "file" "--patterns" "file" "--threshold" "not-number"])))
     (is (nil? (process-input-params ["--data" "file" "--patterns" "file" "--threshold" "-100"])))
     (is (nil? (process-input-params ["--data" "file" "--patterns" "file" "--threshold" "100500"]))))))