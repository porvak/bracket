define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    urlRoot:"#{jsonUri.root}/api/pub/tournament"
    id:"4f41ce03d17060d0d8dbd4d6"
  )