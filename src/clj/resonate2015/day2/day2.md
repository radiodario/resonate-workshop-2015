# Day 2 Workshop notes
* always slice problems in to tiny functions, and build from one to many

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


## Reading list
[Understanding Entity-component Systems](http://gamedev.net/page/resources/_/technical/game-programming/understanding-component-entity-systems-r3013)
[Clojure component - Stuart Sierra](http://github.com/stuartsierra/component)
[Juxt - Modular](http://github.com/juxt/modular)
