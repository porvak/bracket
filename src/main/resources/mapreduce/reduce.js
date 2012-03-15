function (key, values) {
    var result = {score:0, pointsCounted:0, maxPointsPossible:0};
    values.forEach( function(v) {
        result.pointsCounted += v.pointsCounted;
        result.score += v.score;
        result.maxPointsPossible = v.maxPointsPossible;
    });
    return result;
}