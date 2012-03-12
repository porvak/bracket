(function() {
  var __hasProp = Object.prototype.hasOwnProperty;

  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/percentComplete.html'], function(Backbone, $, handlebars, strPercentComplete) {
    return Backbone.View.extend({
      initialize: function(options) {
        return this.percentHB = handlebars.compile(strPercentComplete);
      },
      render: function(teamViewArr, scoreView) {
        if (this.$el.html()) {
          return this.$el.html($(this.percentHB({
            percent: this.calculate(teamViewArr, scoreView)
          })).html());
        } else {
          this.$el = $(this.percentHB({
            percent: this.calculate(teamViewArr, scoreView)
          }));
          return $('#bracketNode').append(this.$el);
        }
      },
      calculate: function(arr, scoreView) {
        var complete, locator, teamView, totalPicks, _ref;
        complete = 0;
        totalPicks = -1;
        for (locator in arr) {
          if (!__hasProp.call(arr, locator)) continue;
          teamView = arr[locator];
          if (!teamView.model.get('teamId')) {
            totalPicks++;
            if ((_ref = teamView.model.get('userPick')) != null ? _ref.teamId : void 0) {
              complete++;
            }
          }
        }
        totalPicks++;
        if (scoreView.model.get('tieBreaker')) complete++;
        return "" + (Math.round((complete / totalPicks) * 100)) + "%";
      }
    });
  });

}).call(this);
