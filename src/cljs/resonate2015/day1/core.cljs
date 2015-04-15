(ns resonate2015.day1.core
  (:require-macros
    [cljs-log.core :refer [info warn]]
    [reagent.ratom :refer [reaction]])
  (:require
    [thi.ng.geom.core :as g]
    [thi.ng.geom.core.vector :as v :refer [vec2]]
    [thi.ng.common.math.core :as m]
    [thi.ng.geom.webgl.animator :refer [animate]]
    [resonate2015.day1.ecs :as ecs :refer [run-system]]
    [cljsjs.react :as react]
    [reagent.core :as reagent :refer [atom]]
    [re-frame.core :refer [register-handler
                           register-sub
                           subscribe
                           dispatch
                           trim-v]]
    ))

;; add a handler for the `:add-particles` event
(register-handler
  :add-particles
  ;; the first argument is event name (add-particles),
  ;; second argument is the number of particles to add
  (fn [db [_ n]]
    ;; we reduct
    (reduce
      (fn [db _]
        (ecs/create-entity
          db
          { :pos (v/randvec2 640)
            :vel (v/randvec2 (m/random 1 6))
            ;; let's render all particles for the moment
            :render? true }))
      db (range n))))

(register-sub
  :particle-count
  (fn [db _] (reaction (count (:entities @db))))
  )


(defn particle-count
  []
  (let [num (subscribe [:particle-count])]
    [:div "Particles: " @num
      [:button {:on-click (fn [] (dispatch [:add-particles 10]))}
        "Click me!"
      ]
    ]
  )
)

#_(defn make-particles
  "Makes a number of particles"
  [app n]
  ;; do n times something
  (dotimes [i n]
    (ecs/update-frame!
      app
        {:pos (v/randvec2 640)
         :vel (v/randvec2 (m/random 1 6))
         :render? true ;let's render all particles for the moment
         })))

(defn move-particle
  "Moves a particle on the system"
  [app id state]
  ;; we extract the velocity from the state
  (swap! app update-in [:entities id :pos] g/+ (:vel state)))


(defn jitter-particle
  "doc-string"
  [arg-list]
  )

(defn draw-particle
  "draws a particle"
  [ctx]
  ;; destructure only to get the pos of the state
  ;; it's the only thing we're interested in
  (fn [_ _ {:keys [pos] :as state}]
    (.fillRect ctx (:x pos) (:y pos) 3 3)
    ))

;; swizling vec2.xyz vec2.xx

(defn main
  []


  )


#_(defn main
  "The main app function"
  []
  (let [canvas (.createElement js/document "canvas")
        ctx (.getContext canvas "2d")
        w 640
        h 480]
      ;; fields of an object are accessed with `.-`
      ;; and changed with set!
      (set! (.-width canvas) w)
      (set! (.-height canvas) h)
      (set! (.-strokeStyle ctx) "white")
      (set! (.-fillStyle ctx) "#000022")
      (.fillRect ctx 0 0 w h)
      (.appendChild (.-body js/document) canvas)
      (make-particles app 100)
      ;; we add a new system for our mover particles
      (ecs/new-system! app :movers #{:pos :vel} move-particle)
      (ecs/new-system! app :render #{:render?} (draw-particle ctx))

      (reagent/render-component
        [particle-count] (.getElementById js/document "app"))

      #_(animate
        ;; animate must return a truthy value to continue,
        ;; otherwise it'll die (which is useful to stop animations)
        (fn []
          ;; clear the screen first
          (set! (.-fillStyle ctx) "#000022")
          (.fillRect ctx 0 0 w h)
          (set! (.-fillStyle ctx) "#f0f")
          (run-system app :render)
          (run-system app :movers)
          true))))


#_(defn foo [])


(main)