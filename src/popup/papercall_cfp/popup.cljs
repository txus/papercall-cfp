(ns papercall-cfp.popup
  (:require-macros [chromex.support :refer [runonce]])
  (:require [papercall-cfp.popup.core :as core]))

(runonce
  (core/init!))
