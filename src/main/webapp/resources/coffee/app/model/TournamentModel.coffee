define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    urlRoot:"#{jsonUri.root}/tournament"
    id:"1"
  )