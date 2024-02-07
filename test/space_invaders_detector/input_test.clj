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
