(ns stealthy.core
  (:require))

(enable-console-print!)

(println "This text is printed from src/stealthy/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

(def grid [[:empty :empty :empty :empty :empty :empty]
           [:empty :empty :empty :empty :empty :empty]
           [:empty :empty :empty :full :empty :empty]
           [:empty :empty :empty :empty :empty :empty]
           [:empty :empty :empty :full :empty :empty]
           [:full :empty :empty :empty :empty :empty]])

(defn abs [x]
  (if (< x 0) (* x -1) x))

(defn round [x]
  (int (+ x 0.5)))

(defn sqrt [x]
  (.sqrt js/Math x))

(defn pow [x y]
  (.pow js/Math x y))


(defn distance [x0 y0 x1 y1]
  (sqrt (+ (pow (- x1 x0) 2)
           (pow (- y1 y0) 2))))

; seq of all points on line between xs and ys
(defn line-dda [x0 y0 x1 y1]
  (let [dx (- x1 x0)
        dy (- y1 y0)
        steps (if (> dx dy) (abs dx)
                            (abs dy))
        xinc (/ dx steps)
        yinc (/ dy steps)]
      (map #(vector (round (* xinc %)) (round (* yinc %)))
           (range steps))))

(defn point-at-dist [x0 y0 x1 y1 dist]
  (let [t (/ dist (distance x0 y0 x1 y1))]
    (vector (+ (* t x1) (* (- 1 t) x0))
            (+ (* t y1) (* (- 1 t) y0)))))


; where does a ray starting at x-start/y-start and traveling in the direction of
; x-dir/y-dir in grid hit a :full wall?
(defn ray-end-point [grid max x-start y-start x-dir y-dir]
  (let [ray-points (apply line-dda x-start y-start (point-at-dist x-start y-start x-dir y-dir max))
        first-wall (some #(if (= :full (get-in grid %)) %)
                         ray-points)]
    (or first-wall
        (point-at-dist x-start y-start x-dir y-dir max))))


(print (ray-end-point grid 10 0 0 1 1))

