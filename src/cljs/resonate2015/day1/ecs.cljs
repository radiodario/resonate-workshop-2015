(ns resonate2015.day1.ecs
  (:require
    [clojure.set :as set]))

;; (component :position {:x 0 :y 10})

;; 1 [pos col vel renderable] ;; player
;; 2 [skill pos vel]

;; 1 {:pos [1 0] :color :red :renderable true}
;; 2 {:pos [1 0 ] :vel [1 0]

;; color
;; vel

;; {1 [{:name :x :y} {:name :color} ...]
;;  2 [{:name :x :y}]
;;  }

(defn make-app
  []
  (atom {:uuid-counter 0
         :entities     {}
         :systems      {}}))

(defn component
  "Defines a new component"
  [app-state entity-id name state]
  ;; assoc-in builds the data structure for
  ;; the path you've passed as the second argument
  ;; with whatever is the third argument
  (assoc-in app-state [:entities entity-id name] state))

(defn update-entity
  "Updates a app state map with new entity states."
  [app-state entity-id states]
  (update-in app-state [:entities entity-id]
         merge states))

(defn create-entity
  [app-state states]
    (let [eid (inc (:uuid-counter app-state))]
      (-> app-state
          (assoc :uuid-counter eid)
          ;; we call the 3-arity version of the function
          (update-entity eid states))))

;; exclamation mark is convention to signal a mutating
;; function
(defn update-entity!
  "Swaps a app state atom with new entity states."
  [app-state entity-id states]
  ;; merge merges two maps
  ;; mutate comps table by updating on the entity id route
  ;; whatever states we've passed
  (swap! app-state
         update-in [:entities entity-id]
         merge states))

(defn create-entity!
  [app-state states]
    (update-entity!
      app-state
      ;; this will get executed earlier
      (new-entity! app-state)
      states))

(defn new-entity!
  "Defines a new entity ID"
  [app-state]
  ;; it's like `-->` but inverted (useful for updates)
  (-> app-state
      (swap! update-in [:uuid-counter] inc)
      (:uuid-counter)))

;; the above is equal to this:
(comment
	(defn new-entity!
	  [app-state]
	  (:uuid-counter (swap! app-state update-in [:uuid-counter] inc)))
)


(defn new-system!
  "Defines a new system in the given app-state atom. Takes a system name
component ids and a system function to process them"
  [app-state name comp-ids f]
  ;; we add the system to the app-state under its name
  (swap! app-state
         assoc-in [:systems name]
         {:sys-fn f
          :comp-ids comp-ids}))


(defn make-entity-validator
  "Creates a higher-order function that allows us to validate entities"
  [comp-ids]
  (let [
        ;; We create a
        ;; partial function that uses the `subset?` function
        ;; to check if a set of keys is a subset of the required
        ;; keys for processing that entity
        valid-entity-keys? (partial set/subset? comp-ids)]
    ;; now we return a function
    ;; that applies `valid-entity-keys` to each de-structured
    ;; object that we've destructured as a sequence.
    ;;
    (fn [[_ v]] (valid-entity-keys? (set (keys v))))))


(defn run-system
  "Run a single system function on matching components
called like: `(run-system app-state :render)`"
  [app-state name]
  (let [
        ;; destructure inside the let to access entities and
			  ;; systems from the app-state, by dereferencing it with @
			  ;; (could also be done with `(deref app-state)`, useful when
			  ;; derefing a list of atoms
        {:keys [entities systems]} (deref app-state)
        ;; look inside the current system and destructure the
        ;; system function and the comp-ids
        {:keys [sys-fn comp-ids]} (systems name)
        ;; create an entity validator for this system's required comp keys
        valid-entity? (make-entity-validator comp-ids)
        ;; now we filter the entities to those which have
        ;; all of our matching components.
        matching-entities (filter valid-entity? entities)]
   ;; since map/reduce are lazy, we must use a loop to
   ;; update/apply the functions. we can also use `doall`, `dorun` (for side effects)
   (dorun
     ;; map to every matching entity
     (map
       ;; we dereference the entity id from its state, and create
       ;; an anonymous function that applies the system-function
       ;; to each entity state
       (fn [[eid estate]] (sys-fn app-state eid estate))
       matching-entities))))

;; test
(defn movement-system
  "function for the movement system"
  [app-state eid estate]
  ;; destructure out the position and velocity from the estate
  (let [{:keys [pos vel]} estate
        ;; create a new position vector by mapping `+` to all
        ;; elements of our positions and velocity vectors.
        ;; we use mapv because it's not lazy and returns a vector
        pos' (mapv + pos vel)]
    (swap! app-state assoc-in [:entities eid :pos] pos')))



