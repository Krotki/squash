package org.jetbrains.squash.dialects.postgres

import org.jetbrains.squash.expressions.Expression
import org.jetbrains.squash.expressions.literal

//fun <V: PgJsonValue?> Expression<V>.get(index: Int) = JsonElementExpression(this, index.toString())
//fun <V: PgJsonValue?> Expression<V>.get(key: String) = JsonElementExpression(this, key)
//fun <V: PgJsonValue?> Expression<V>.getAsText(index: Int) = JsonElementAsTextExpression(this, index.toString())
//fun <V: PgJsonValue?> Expression<V>.getAsText(key: String) = JsonElementAsTextExpression(this, key)
fun <V : PgJson?> Expression<V>.path(vararg path: String) = JsonPathAsTextExpression(this, literal(TextArray(path)))
//fun <V: PgJsonValue?> Expression<V>.pathAsText(vararg path: String) = JsonPathAsTextExpression(this, PgJsonPath(path))

//fun <V: PgJsonValue?> JsonPathExpression<V>.text() = JsonPathAsTextExpression(this.left, this.right)
fun <V : PgJson?> JsonPathAsTextExpression<V>.node() = JsonPathExpression(this.left, this.right)

operator fun <V : PgJson?> Expression<V>.get(vararg path: String) = JsonPathAsTextExpression(this, literal(TextArray(path)))

infix fun Expression<JsonB?>.contains(json: Expression<JsonB?>) = JsonContainsExpression(this, json)
infix fun Expression<JsonB?>.contains(json: JsonB?) = JsonContainsExpression(this, literal(json))
infix fun Expression<JsonB?>.contains(json: String) = JsonContainsExpression(this, literal(JsonB(json)))

infix fun Expression<JsonB?>.containedIn(json: Expression<JsonB?>) = JsonContainedInExpression(this, json)
infix fun Expression<JsonB?>.containedIn(json: JsonB?) = JsonContainedInExpression(this, literal(json))
infix fun Expression<JsonB?>.containedIn(json: String) = JsonContainedInExpression(this, literal(JsonB(json)))

infix fun Expression<JsonB?>.containsKey(key: String) = JsonContainsKeyExpression(this, literal(key))
infix fun Expression<JsonB?>.anyKey(keys: Array<String>) = JsonContainsAnyKeyExpression(this, literal(TextArray(keys)))
infix fun Expression<JsonB?>.allKeys(keys: Array<String>) = JsonContainsAllKeysExpression(this, literal(TextArray(keys)))