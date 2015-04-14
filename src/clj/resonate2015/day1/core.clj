(ns resonate2015.day1.core)

(defn hello
  [] (prn "zdravo"))

(def my-map {:a {:b {:c [1 2 3] :d "string"}}})

(get-in my-map [:a :b :d 2])

(def my-set #{})

(time (conj my-set 1 2 3 4 5 5 1 1 1 1 1 1))

(filter #{:name :age} (keys {:name "dario" :age 29 :foo "bar"}))

(seq #{:a :b :c})

(map (fn [x] (prn (x 0))) {:a 1 :b 2 :c 3 :d 5 :e 4})

(take 6 (iterate (fn [x] (* x x)) 2))

(require '[thi.ng.geom.core :as g])
(require '[thi.ng.geom.aabb :as a])
