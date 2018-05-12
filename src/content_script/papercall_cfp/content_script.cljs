(ns papercall-cfp.content-script
  (:require-macros [chromex.support :refer [runonce]])
  (:require [papercall-cfp.content-script.core :as core]))

(runonce
  (core/init!))
