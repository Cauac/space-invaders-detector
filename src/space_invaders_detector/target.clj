(ns space-invaders-detector.target)

(defprotocol Target
  (includes-point? [this y x] "True if the specified point is a part of the target")
  (accurate? [this limit] "Thue if target meets the accuracy limit")
  (translation-location [this offset] "Shifts target location with provided offset"))

(defrecord LocatedInvader [location data accuracy]
  Target
  (includes-point? [_ y x]
    (let [[y1 x1 y2 x2] location]
      (and (<= y1 y y2) (<= x1 x x2))))
  (accurate? [_ limit]
    (<= limit accuracy))
  (translation-location [_ offset]
    (LocatedInvader. (mapv #(- % offset) location) data accuracy))
  )