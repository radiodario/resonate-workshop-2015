(ns resonate2015.day1.ecs)

;; kebab-case-is-like-this

;; our app state - atoms are persistent
;; atomic means that if 'swap' failed nothing will break
;; TODO read about this
;; TODO read about agents, atoms and refs
(def app 
  (atom {
     :uuid-counter 0
     :entities {}
     :systems {}
   }))


(defn component
  "Defines a new component"
  [app-state entity-id name state]
  ;; assoc-in builds the data structure for
  ;; the path you've passed as the second argument
  ;; with whatever is the third argument
  (assoc-in app-state [:entities entity-id name] state))

;; exclamation mark is mutating
(defn add-components-for-eid!
  "swaps! an app state atom with new state for an etity"
  [app-state entity-id states]
  ;; merge merges two maps
  ;; mutate comps table by updating on the entity id route
  ;; whatever states we've passed
  (swap! app-state 
         update-in [:entities entity-id] 
         merge states))

(defn add-components-for-eid
  "Adds componentsto entity id, but not mutating the comps-table"
  [app-state entity-id states]
  (update-in app-state [:entities entity-id] 
             merge states))

(defn new-entity!
  "Defines a new entity ID and increases the entity counter"
  [app-state]
  (-> ;; it's like --> but inverted (useful for updates)
    app-state
    (swap! update-in [:uuid-counter] inc)
    (:uuid-counter)
  ))

;; the above is equal to this:
(comment
	(defn new-entity!
	  [app-state]
	  (:uuid-counter (swap! app-state update-in [:uuid-counter] inc)))
)


(defn system
  "Defines a new system"
  [name f])
