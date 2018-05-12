(ns papercall-cfp.content-script.core
  (:require [goog.string :as gstring]
            [chromex.logging :refer-macros [log info warn error group group-end]]))

(defn- get-button [type]
  (aget (js/document.getElementsByClassName (str "happy__label--" (name type))) 0))

(defn- save! []
  (log "Saving...")
  (.click
   (.querySelector
    (js/document.getElementById "ratings-section")
    ".btn--blue-l")))

(defn- next! []
  (log "Next submission...")
  (let [url
        (.-href
         (first
          (filter
           (fn [elem]
             (= (.-innerText elem) "Next Submission "))
           (array-seq
            (js/document.getElementsByClassName "link")
            0))))]
    (js/setTimeout
     (aset js/window "location" url)
     2000)))

(defn upvote []
  (.click (get-button :yes))
  (save!)
  (next!))

(defn downvote []
  (.click (get-button :no))
  (save!)
  (next!))

(defn maybevote []
  (.click (get-button :maybe))
  (save!)
  (next!))

; -- main entry point -------------------------------------------------------------------------------------------------------

(defn speech! []
  (let [re (new js/webkitSpeechRecognition)]
    (aset re "continuous" true)
    (aset re "interimResults" false)
    (aset re "lang" "en_US")
    (aset re "onstart" (fn [x]))
    (aset re "onresult" (fn [x]
                          (let [results (aget x "results")
                                last-idx (dec (aget results "length"))
                                last-result (aget results (str last-idx))
                                first-alternative (aget last-result "0")
                                utterance (aget first-alternative "transcript")]
                            (log (str "Got: " utterance))
                            (cond
                              (or (gstring/endsWith utterance "yes")
                                  (gstring/endsWith utterance "great")
                                  (gstring/endsWith utterance "good")
                                  (gstring/endsWith utterance "cool"))
                              (upvote)

                              (or (gstring/endsWith utterance "s***")
                                  (gstring/endsWith utterance "no"))
                              (downvote)

                              (gstring/endsWith utterance "maybe")
                              (maybevote)

                              :else (log (str "I don't understand: " utterance))))))
    (aset re "onend" (fn [x]
                       (log "ended." x)))
    (aset re "onerror" (fn [x]
                         (log "error... re-starting")
                         (js/setTimeout
                          #(speech!)
                          2000)))
    (log "starting recognition")
    (.start re)))

(defn init! []
  (speech!))
