package org.jetbrains.squash.dialects.postgres

import org.jetbrains.squash.expressions.BinaryExpression
import org.jetbrains.squash.expressions.Expression
import org.jetbrains.squash.expressions.literal


// -> operator
class JsonElementExpression<out V : PgJson?>(left: Expression<V>, key: String)
    : BinaryExpression<V, String, V>(left, literal(key))

// ->> operator
class JsonElementAsTextExpression<out V : PgJson?>(left: Expression<V>, key: String)
    : BinaryExpression<V, String, String>(left, literal(key))

// #> operator
class JsonPathExpression<out V : PgJson?>(left: Expression<V>, right: Expression<TextArray>)
    : BinaryExpression<V, TextArray, V>(left, right)

// #>> operator
class JsonPathAsTextExpression<out V : PgJson?>(left: Expression<V>, right: Expression<TextArray>)
    : BinaryExpression<V, TextArray, String>(left, right)

// @> operator
class JsonContainsExpression<out V : JsonB?>(left: Expression<V>, right: Expression<V>)
    : BinaryExpression<V, V, Boolean>(left, right)

// <@ operator
class JsonContainedInExpression<out V : JsonB?>(left: Expression<V>, right: Expression<V>)
    : BinaryExpression<V, V, Boolean>(left, right)

// ? operator
class JsonContainsKeyExpression(left: Expression<JsonB?>, right: Expression<String>)
    : BinaryExpression<JsonB?, String, Boolean>(left, right)

// ?| operator
class JsonContainsAnyKeyExpression(left: Expression<JsonB?>, right: Expression<TextArray>)
    : BinaryExpression<JsonB?, TextArray, Boolean>(left, right)

// ?& operator
class JsonContainsAllKeysExpression(left: Expression<JsonB?>, right: Expression<TextArray>)
    : BinaryExpression<JsonB?, TextArray, Boolean>(left, right)