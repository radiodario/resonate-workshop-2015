# Day 1 workshop notes

* use doubles on processing, rather than flaots
* in clojure, to create a list of numbers, you use a quoted list which gets interpreted verbatim `'(1 2 3 4)`, or use the `list` function
* quoting means don't evaluate, but `list` will be evaluated
* `eval` isn't as taxing as in javascript because what is passed is already a data structure
* All data is immutable. Change in a system must be represented via immutable data structures`.
* Any function used as a *question* (returns true or false) is usually written with a question mark at the end (i.e. `coll?`)
* Package management is done via Clojars

## Vectors
Vectors use square brackets. They are always data (no need to first thing to be function). used for variables in sequential collection. closest to an array: `(def a [1 2 3 4])`
    - vectors can be used as functions, to access the nth element you write: `(a 0)`
    - `(count a)` to count elements
    - `(subvec a from to)` get everything from the fromth element to the toth element
    - `(conj a "hello")` adds the second argument to the collection. It is polymorphic function which will do the most efficient thing:
        + `(conj [1 2 3] 4)` -- puts 4 at the end
        + `(conj (1 2 3) 4)` -- puts 4 at the begining
    - `(vec ...)` allows generating vectors
    - `(vec (range))` creates a vector from a `range`

## Hashmaps
Key-value pair storage. In clojure, anything can be a key, hashing function has very low collision.
Prefix a symbol with colon to create a keywords. it's used  for identity checks. uses pointer comparison (super fast). Keys are usually created using keywords

Creating and accessing hashmaps:
```clojure
    (def a {:name "dario" :age 29})
    (a :name) --> "dario"
    (a :age) --> 29
    ;; Note that both orderings are allowed:
    (:name a) --> "dario"
    ;; Fields can be accessed with get,
    (get a :name) --> "dario"
```

There are two falsy values: `false` and `nil`, everything else evaluates to `true` (i.e. *truthyness*)

```clojure
    (def peeps [{:age 18 :name "szymon"} {:age 29 :name "dario"}])
    ;; in order to get a list of ages, you can use a keyword as a function
    (map :age peeps)
    ;; get keys
    (keys (first peeps))
    ;; reverse an object (keys are now the values and vice versa)
    (apply hash-map (mapcat reverse (seq {:name "dario" :age 29})))
    ;; map returns a list per pair, mapcat flattens one level
```

## Flattening
```clojure
    ;; the flatten function flattens any depth
    (flatten [[[[123] [5 6 7]] [a] 2 3 5] 18])
    ;; => (123 5 6 7 {:name "dario"} 2 3 5 18)
```

## Ranges
The `range` function creates lazy sequences (on demand) - it *yields* - it becomes a recipe for a computation
```clojure
    (range 10)
    ;; creates an infinite sequence
    (range)
    ;; from 10 to 100 in steps for five
    (range 10 100 5)
```

## Lambdas / filtering
Most comprehensive functions evaluate lazily. Anonymous functions can be written with the `fn` keyword, and can be passed as arguments.
```clojure
    ;; filter only divisible by 5
    (filter (fn [x] (zero? (mod x 5))) (range 1000))
    ;; get the first 1000 numbers divisible by 5
    (take 1000 (filter (fn [x] (zero? (mod x 5))) (range)))
```

## Piping (slerp)
The `->>` keyword allows to create "flows" which are more readable. The intepreter rewrites the input.
```
    (->> (range)
         (filter (fn [x] (zero? (mod x 5))))
         (drop 100)
         (take 10)
    )
```

## Closures
Closures work like in javascript
```
    (def make-greeter [greeting] (fn [name] (print greeting name)))
    (def greetings-en (make-greeter "hello"))
    (def greetings-de (make-greeter "hallo"))
    (greetings-en "paul")
    ; hello paul
    (greetings-de "paul")
    ; hallo paul
```

## Reduce
Takes a collection and reduces to a single value
```
    (reduce (fn [acc x] (+ acc x)) (range 10))
```
the `reductions` function allows you to trace through the iterations

## Scope and let
```
    (def a 23)
    ;; a is 23
    ;; lets are processed sequentially
    (let [a 42 ;; but now it's `42` here
          b (+ a 10) ;; so b is set to 52
          a :foo] ;; and now we set a to `:foo`
      b) ;; 52
```

## Partials
Partial functions allow currying, that is, hard-coding one argument of the function
```
    (defn foo
        [a b c d]
        )
    (partial foo "something")
```

## Macros and requiring
Macros are run at compile time. You can "derefrence" a library at require time by saying `(:require [my.library :as [thing other-thing]])`


## Reading
* Chris Okazaki - Purely Functional Data Structures (1996)
* [Persistent vectors in clojure](hypirion.com/musings/understanding-persistent-vector-pt-1)
* [Understanding Entity-component Systems](http://gamedev.net/page/resources/_/technical/game-programming/understanding-component-entity-systems-r3013)
* [Clojure component - Stuart Sierra](http://github.com/stuartsierra/component)
* [Juxt - Modular](http://github.com/juxt/modular)
* [Reframe - React best practises for clojurescript]
* [Reagent project](http://reagent-project.github.io)