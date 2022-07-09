package yael.laguna.tsunami_draft

data class Quake(var Date : String ?= null, var Depth : Int ?= null,
                 var Earthquake_ID : Float ?= null, var Hyperlink : String ?= null,
                 var Latitude : Double ?= null, var Longitude : Double ?= null,
                 var Magnitude : Double ?= null, var Region : String ?= null,
                 var Tsunami_Message : String ?= null)
