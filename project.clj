(defproject space-invaders-detector "0.1.0-SNAPSHOT"
  :description "Space invaders detector"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clojure-term-colors "0.1.0"]]
  :main space-invaders-detector.core
  :profiles {:uberjar {:uberjar-name "detector.jar"
                       :aot :all}}
  )
