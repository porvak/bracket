define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    urlRoot:"#{jsonUri.root}/api/pub/leaderboard/pool/"
    id:"4f3c8297a0eea26b78d77538"
  )