package cn.sexycode.myjpa.query.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qzz
 */
public class MybatisCriteriaBuilder implements CriteriaBuilder {
    private EntityManager entityManager;

    public MybatisCriteriaBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Create a <code>CriteriaQuery</code> object.
     *
     * @return criteria query object
     */
    @Override
    public CriteriaQuery<Object> createQuery() {
        return new DefaultFilter<>(this, entityManager);
    }

    /**
     * Create a <code>CriteriaQuery</code> object with the specified result
     * type.
     *
     * @param resultClass type of the query result
     * @return criteria query object
     */
    @Override
    public <T> CriteriaQuery<T> createQuery(Class<T> resultClass) {
        return new DefaultFilter<>(this, entityManager, resultClass);
    }

    /**
     * Create a <code>CriteriaQuery</code> object that returns a tuple of
     * objects as its result.
     *
     * @return criteria query object
     */
    @Override
    public CriteriaQuery<Tuple> createTupleQuery() {
        return null;
    }

    /**
     * Create a <code>CriteriaUpdate</code> query object to perform a bulk update operation.
     *
     * @param targetEntity target type for update operation
     * @return the query object
     * @since Java Persistence 2.1
     */
    @Override
    public <T> CriteriaUpdate<T> createCriteriaUpdate(Class<T> targetEntity) {
        return null;
    }

    /**
     * Create a <code>CriteriaDelete</code> query object to perform a bulk delete operation.
     *
     * @param targetEntity target type for delete operation
     * @return the query object
     * @since Java Persistence 2.1
     */
    @Override
    public <T> CriteriaDelete<T> createCriteriaDelete(Class<T> targetEntity) {
        return null;
    }

    /**
     * Create a selection item corresponding to a constructor.
     * This method is used to specify a constructor that will be
     * applied to the results of the query execution. If the
     * constructor is for an entity class, the resulting entities
     * will be in the new state after the query is executed.
     *
     * @param resultClass class whose instance is to be constructed
     * @param selections  arguments to the constructor
     * @return compound selection item
     * @throws IllegalArgumentException if an argument is a
     *                                  tuple- or array-valued selection item
     */
    @Override
    public <Y> CompoundSelection<Y> construct(Class<Y> resultClass, Selection<?>... selections) {
        return null;
    }

    /**
     * Create a tuple-valued selection item.
     *
     * @param selections selection items
     * @return tuple-valued compound selection
     * @throws IllegalArgumentException if an argument is a
     *                                  tuple- or array-valued selection item
     */
    @Override
    public CompoundSelection<Tuple> tuple(Selection<?>... selections) {
        return null;
    }

    /**
     * Create an array-valued selection item.
     *
     * @param selections selection items
     * @return array-valued compound selection
     * @throws IllegalArgumentException if an argument is a
     *                                  tuple- or array-valued selection item
     */
    @Override
    public CompoundSelection<Object[]> array(Selection<?>... selections) {
        return null;
    }

    /**
     * Create an ordering by the ascending value of the expression.
     *
     * @param x expression used to define the ordering
     * @return ascending ordering corresponding to the expression
     */
    @Override
    public Order asc(Expression<?> x) {
        return null;
    }

    /**
     * Create an ordering by the descending value of the expression.
     *
     * @param x expression used to define the ordering
     * @return descending ordering corresponding to the expression
     */
    @Override
    public Order desc(Expression<?> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the avg operation.
     *
     * @param x expression representing input value to avg operation
     * @return avg expression
     */
    @Override
    public <N extends Number> Expression<Double> avg(Expression<N> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the sum operation.
     *
     * @param x expression representing input value to sum operation
     * @return sum expression
     */
    @Override
    public <N extends Number> Expression<N> sum(Expression<N> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the sum operation to an
     * Integer-valued expression, returning a Long result.
     *
     * @param x expression representing input value to sum operation
     * @return sum expression
     */
    @Override
    public Expression<Long> sumAsLong(Expression<Integer> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the sum operation to a
     * Float-valued expression, returning a Double result.
     *
     * @param x expression representing input value to sum operation
     * @return sum expression
     */
    @Override
    public Expression<Double> sumAsDouble(Expression<Float> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the numerical max
     * operation.
     *
     * @param x expression representing input value to max operation
     * @return max expression
     */
    @Override
    public <N extends Number> Expression<N> max(Expression<N> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the numerical min
     * operation.
     *
     * @param x expression representing input value to min operation
     * @return min expression
     */
    @Override
    public <N extends Number> Expression<N> min(Expression<N> x) {
        return null;
    }

    /**
     * Create an aggregate expression for finding the greatest of
     * the values (strings, dates, etc).
     *
     * @param x expression representing input value to greatest
     *          operation
     * @return greatest expression
     */
    @Override
    public <X extends Comparable<? super X>> Expression<X> greatest(Expression<X> x) {
        return null;
    }

    /**
     * Create an aggregate expression for finding the least of
     * the values (strings, dates, etc).
     *
     * @param x expression representing input value to least
     *          operation
     * @return least expression
     */
    @Override
    public <X extends Comparable<? super X>> Expression<X> least(Expression<X> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the count operation.
     *
     * @param x expression representing input value to count
     *          operation
     * @return count expression
     */
    @Override
    public Expression<Long> count(Expression<?> x) {
        return null;
    }

    /**
     * Create an aggregate expression applying the count distinct
     * operation.
     *
     * @param x expression representing input value to
     *          count distinct operation
     * @return count distinct expression
     */
    @Override
    public Expression<Long> countDistinct(Expression<?> x) {
        return null;
    }

    /**
     * Create a predicate testing the existence of a subquery result.
     *
     * @param subquery subquery whose result is to be tested
     * @return exists predicate
     */
    @Override
    public Predicate exists(Subquery<?> subquery) {
        return null;
    }

    /**
     * Create an all expression over the subquery results.
     *
     * @param subquery subquery
     * @return all expression
     */
    @Override
    public <Y> Expression<Y> all(Subquery<Y> subquery) {
        return null;
    }

    /**
     * Create a some expression over the subquery results.
     * This expression is equivalent to an <code>any</code> expression.
     *
     * @param subquery subquery
     * @return some expression
     */
    @Override
    public <Y> Expression<Y> some(Subquery<Y> subquery) {
        return null;
    }

    /**
     * Create an any expression over the subquery results.
     * This expression is equivalent to a <code>some</code> expression.
     *
     * @param subquery subquery
     * @return any expression
     */
    @Override
    public <Y> Expression<Y> any(Subquery<Y> subquery) {
        return null;
    }

    /**
     * Create a conjunction of the given boolean expressions.
     *
     * @param x boolean expression
     * @param y boolean expression
     * @return and predicate
     */
    @Override
    public Predicate and(Expression<Boolean> x, Expression<Boolean> y) {
        DefaultFieldLogic fieldLogic = new DefaultFieldLogic();
        List<WhereClause> whereClauses = fieldLogic.getWhereClauses();
        whereClauses.add((WhereClause) x);
        whereClauses.add((WhereClause) y);
        return fieldLogic;
    }

    /**
     * Create a conjunction of the given restriction predicates.
     * A conjunction of zero predicates is true.
     *
     * @param restrictions zero or more restriction predicates
     * @return and predicate
     */
    @Override
    public Predicate and(Predicate... restrictions) {
        DefaultFieldLogic fieldLogic = new DefaultFieldLogic();
        List<WhereClause> whereClauses = fieldLogic.getWhereClauses();
        whereClauses.addAll(Arrays.stream(restrictions).map(restriction -> (WhereClause) restriction)
                .collect(Collectors.toList()));
        return fieldLogic;
    }

    /**
     * Create a disjunction of the given boolean expressions.
     *
     * @param x boolean expression
     * @param y boolean expression
     * @return or predicate
     */
    @Override
    public Predicate or(Expression<Boolean> x, Expression<Boolean> y) {
        DefaultFieldLogic fieldLogic = new DefaultFieldLogic(FieldRelation.OR);
        fieldLogic.getWhereClauses().add((WhereClause) x);
        fieldLogic.getWhereClauses().add((WhereClause) y);
        return fieldLogic;
    }

    /**
     * Create a disjunction of the given restriction predicates.
     * A disjunction of zero predicates is false.
     *
     * @param restrictions zero or more restriction predicates
     * @return or predicate
     */
    @Override
    public Predicate or(Predicate... restrictions) {
        DefaultFieldLogic fieldLogic = new DefaultFieldLogic(FieldRelation.OR);
        List<WhereClause> whereClauses = fieldLogic.getWhereClauses();
        whereClauses.addAll(Arrays.stream(restrictions).map(restriction -> (WhereClause) restriction)
                .collect(Collectors.toList()));
        return fieldLogic;
    }

    /**
     * Create a negation of the given restriction.
     *
     * @param restriction restriction expression
     * @return not predicate
     */
    @Override
    public Predicate not(Expression<Boolean> restriction) {
        DefaultFieldLogic fieldLogic = new DefaultFieldLogic(FieldRelation.NOT);
        fieldLogic.getWhereClauses().add((WhereClause) restriction);
        return fieldLogic;
    }

    /**
     * Create a conjunction (with zero conjuncts).
     * A conjunction with zero conjuncts is true.
     *
     * @return and predicate
     */
    @Override
    public Predicate conjunction() {
        return new DefaultFieldLogic();
    }

    /**
     * Create a disjunction (with zero disjuncts).
     * A disjunction with zero disjuncts is false.
     *
     * @return or predicate
     */
    @Override
    public Predicate disjunction() {
        return new DefaultFieldLogic(FieldRelation.OR);
    }

    /**
     * Create a predicate testing for a true value.
     *
     * @param x expression to be tested
     * @return predicate
     */
    @Override
    public Predicate isTrue(Expression<Boolean> x) {
        return null;
    }

    /**
     * Create a predicate testing for a false value.
     *
     * @param x expression to be tested
     * @return predicate
     */
    @Override
    public Predicate isFalse(Expression<Boolean> x) {
        return null;
    }

    /**
     * Create a predicate to test whether the expression is null.
     *
     * @param x expression
     * @return is-null predicate
     */
    @Override
    public Predicate isNull(Expression<?> x) {
        return null;
    }

    /**
     * Create a predicate to test whether the expression is not null.
     *
     * @param x expression
     * @return is-not-null predicate
     */
    @Override
    public Predicate isNotNull(Expression<?> x) {
        return null;
    }

    /**
     * Create a predicate for testing the arguments for equality.
     *
     * @param x expression
     * @param y expression
     * @return equality predicate
     */
    @Override
    public Predicate equal(Expression<?> x, Expression<?> y) {
        return null;
    }

    /**
     * Create a predicate for testing the arguments for equality.
     *
     * @param x expression
     * @param y object
     * @return equality predicate
     */
    @Override
    public Predicate equal(Expression<?> x, Object y) {
        return null;
    }

    /**
     * Create a predicate for testing the arguments for inequality.
     *
     * @param x expression
     * @param y expression
     * @return inequality predicate
     */
    @Override
    public Predicate notEqual(Expression<?> x, Expression<?> y) {
        return null;
    }

    /**
     * Create a predicate for testing the arguments for inequality.
     *
     * @param x expression
     * @param y object
     * @return inequality predicate
     */
    @Override
    public Predicate notEqual(Expression<?> x, Object y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than the second.
     *
     * @param x expression
     * @param y expression
     * @return greater-than predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x,
            Expression<? extends Y> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than the second.
     *
     * @param x expression
     * @param y value
     * @return greater-than predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Y y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than or equal to the second.
     *
     * @param x expression
     * @param y expression
     * @return greater-than-or-equal predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x,
            Expression<? extends Y> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than or equal to the second.
     *
     * @param x expression
     * @param y value
     * @return greater-than-or-equal predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Y y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than the second.
     *
     * @param x expression
     * @param y expression
     * @return less-than predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Expression<? extends Y> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than the second.
     *
     * @param x expression
     * @param y value
     * @return less-than predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Y y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than or equal to the second.
     *
     * @param x expression
     * @param y expression
     * @return less-than-or-equal predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x,
            Expression<? extends Y> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than or equal to the second.
     *
     * @param x expression
     * @param y value
     * @return less-than-or-equal predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Y y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * between the second and third arguments in value.
     *
     * @param v expression
     * @param x expression
     * @param y expression
     * @return between predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> v, Expression<? extends Y> x,
            Expression<? extends Y> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * between the second and third arguments in value.
     *
     * @param v expression
     * @param x value
     * @param y value
     * @return between predicate
     */
    @Override
    public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> v, Y x, Y y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than the second.
     *
     * @param x expression
     * @param y expression
     * @return greater-than predicate
     */
    @Override
    public Predicate gt(Expression<? extends Number> x, Expression<? extends Number> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than the second.
     *
     * @param x expression
     * @param y value
     * @return greater-than predicate
     */
    @Override
    public Predicate gt(Expression<? extends Number> x, Number y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than or equal to the second.
     *
     * @param x expression
     * @param y expression
     * @return greater-than-or-equal predicate
     */
    @Override
    public Predicate ge(Expression<? extends Number> x, Expression<? extends Number> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * greater than or equal to the second.
     *
     * @param x expression
     * @param y value
     * @return greater-than-or-equal predicate
     */
    @Override
    public Predicate ge(Expression<? extends Number> x, Number y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than the second.
     *
     * @param x expression
     * @param y expression
     * @return less-than predicate
     */
    @Override
    public Predicate lt(Expression<? extends Number> x, Expression<? extends Number> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than the second.
     *
     * @param x expression
     * @param y value
     * @return less-than predicate
     */
    @Override
    public Predicate lt(Expression<? extends Number> x, Number y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than or equal to the second.
     *
     * @param x expression
     * @param y expression
     * @return less-than-or-equal predicate
     */
    @Override
    public Predicate le(Expression<? extends Number> x, Expression<? extends Number> y) {
        return null;
    }

    /**
     * Create a predicate for testing whether the first argument is
     * less than or equal to the second.
     *
     * @param x expression
     * @param y value
     * @return less-than-or-equal predicate
     */
    @Override
    public Predicate le(Expression<? extends Number> x, Number y) {
        return null;
    }

    /**
     * Create an expression that returns the arithmetic negation
     * of its argument.
     *
     * @param x expression
     * @return arithmetic negation
     */
    @Override
    public <N extends Number> Expression<N> neg(Expression<N> x) {
        return null;
    }

    /**
     * Create an expression that returns the absolute value
     * of its argument.
     *
     * @param x expression
     * @return absolute value
     */
    @Override
    public <N extends Number> Expression<N> abs(Expression<N> x) {
        return null;
    }

    /**
     * Create an expression that returns the sum
     * of its arguments.
     *
     * @param x expression
     * @param y expression
     * @return sum
     */
    @Override
    public <N extends Number> Expression<N> sum(Expression<? extends N> x, Expression<? extends N> y) {
        return null;
    }

    /**
     * Create an expression that returns the sum
     * of its arguments.
     *
     * @param x expression
     * @param y value
     * @return sum
     */
    @Override
    public <N extends Number> Expression<N> sum(Expression<? extends N> x, N y) {
        return null;
    }

    /**
     * Create an expression that returns the sum
     * of its arguments.
     *
     * @param x value
     * @param y expression
     * @return sum
     */
    @Override
    public <N extends Number> Expression<N> sum(N x, Expression<? extends N> y) {
        return null;
    }

    /**
     * Create an expression that returns the product
     * of its arguments.
     *
     * @param x expression
     * @param y expression
     * @return product
     */
    @Override
    public <N extends Number> Expression<N> prod(Expression<? extends N> x, Expression<? extends N> y) {
        return null;
    }

    /**
     * Create an expression that returns the product
     * of its arguments.
     *
     * @param x expression
     * @param y value
     * @return product
     */
    @Override
    public <N extends Number> Expression<N> prod(Expression<? extends N> x, N y) {
        return null;
    }

    /**
     * Create an expression that returns the product
     * of its arguments.
     *
     * @param x value
     * @param y expression
     * @return product
     */
    @Override
    public <N extends Number> Expression<N> prod(N x, Expression<? extends N> y) {
        return null;
    }

    /**
     * Create an expression that returns the difference
     * between its arguments.
     *
     * @param x expression
     * @param y expression
     * @return difference
     */
    @Override
    public <N extends Number> Expression<N> diff(Expression<? extends N> x, Expression<? extends N> y) {
        return null;
    }

    /**
     * Create an expression that returns the difference
     * between its arguments.
     *
     * @param x expression
     * @param y value
     * @return difference
     */
    @Override
    public <N extends Number> Expression<N> diff(Expression<? extends N> x, N y) {
        return null;
    }

    /**
     * Create an expression that returns the difference
     * between its arguments.
     *
     * @param x value
     * @param y expression
     * @return difference
     */
    @Override
    public <N extends Number> Expression<N> diff(N x, Expression<? extends N> y) {
        return null;
    }

    /**
     * Create an expression that returns the quotient
     * of its arguments.
     *
     * @param x expression
     * @param y expression
     * @return quotient
     */
    @Override
    public Expression<Number> quot(Expression<? extends Number> x, Expression<? extends Number> y) {
        return null;
    }

    /**
     * Create an expression that returns the quotient
     * of its arguments.
     *
     * @param x expression
     * @param y value
     * @return quotient
     */
    @Override
    public Expression<Number> quot(Expression<? extends Number> x, Number y) {
        return null;
    }

    /**
     * Create an expression that returns the quotient
     * of its arguments.
     *
     * @param x value
     * @param y expression
     * @return quotient
     */
    @Override
    public Expression<Number> quot(Number x, Expression<? extends Number> y) {
        return null;
    }

    /**
     * Create an expression that returns the modulus
     * of its arguments.
     *
     * @param x expression
     * @param y expression
     * @return modulus
     */
    @Override
    public Expression<Integer> mod(Expression<Integer> x, Expression<Integer> y) {
        return null;
    }

    /**
     * Create an expression that returns the modulus
     * of its arguments.
     *
     * @param x expression
     * @param y value
     * @return modulus
     */
    @Override
    public Expression<Integer> mod(Expression<Integer> x, Integer y) {
        return null;
    }

    /**
     * Create an expression that returns the modulus
     * of its arguments.
     *
     * @param x value
     * @param y expression
     * @return modulus
     */
    @Override
    public Expression<Integer> mod(Integer x, Expression<Integer> y) {
        return null;
    }

    /**
     * Create an expression that returns the square root
     * of its argument.
     *
     * @param x expression
     * @return square root
     */
    @Override
    public Expression<Double> sqrt(Expression<? extends Number> x) {
        return null;
    }

    /**
     * Typecast.  Returns same expression object.
     *
     * @param number numeric expression
     * @return Expression&#060;Long&#062;
     */
    @Override
    public Expression<Long> toLong(Expression<? extends Number> number) {
        return null;
    }

    /**
     * Typecast.  Returns same expression object.
     *
     * @param number numeric expression
     * @return Expression&#060;Integer&#062;
     */
    @Override
    public Expression<Integer> toInteger(Expression<? extends Number> number) {
        return null;
    }

    /**
     * Typecast. Returns same expression object.
     *
     * @param number numeric expression
     * @return Expression&#060;Float&#062;
     */
    @Override
    public Expression<Float> toFloat(Expression<? extends Number> number) {
        return null;
    }

    /**
     * Typecast.  Returns same expression object.
     *
     * @param number numeric expression
     * @return Expression&#060;Double&#062;
     */
    @Override
    public Expression<Double> toDouble(Expression<? extends Number> number) {
        return null;
    }

    /**
     * Typecast.  Returns same expression object.
     *
     * @param number numeric expression
     * @return Expression&#060;BigDecimal&#062;
     */
    @Override
    public Expression<BigDecimal> toBigDecimal(Expression<? extends Number> number) {
        return null;
    }

    /**
     * Typecast.  Returns same expression object.
     *
     * @param number numeric expression
     * @return Expression&#060;BigInteger&#062;
     */
    @Override
    public Expression<BigInteger> toBigInteger(Expression<? extends Number> number) {
        return null;
    }

    /**
     * Typecast.  Returns same expression object.
     *
     * @param character expression
     * @return Expression&#060;String&#062;
     */
    @Override
    public Expression<String> toString(Expression<Character> character) {
        return null;
    }

    /**
     * Create an expression for a literal.
     *
     * @param value value represented by the expression
     * @return expression literal
     * @throws IllegalArgumentException if value is null
     */
    @Override
    public <T> Expression<T> literal(T value) {
        return null;
    }

    /**
     * Create an expression for a null literal with the given type.
     *
     * @param resultClass type of the null literal
     * @return null expression literal
     */
    @Override
    public <T> Expression<T> nullLiteral(Class<T> resultClass) {
        return null;
    }

    /**
     * Create a parameter expression.
     *
     * @param paramClass parameter class
     * @return parameter expression
     */
    @Override
    public <T> ParameterExpression<T> parameter(Class<T> paramClass) {
        return null;
    }

    /**
     * Create a parameter expression with the given name.
     *
     * @param paramClass parameter class
     * @param name       name that can be used to refer to
     *                   the parameter
     * @return parameter expression
     */
    @Override
    public <T> ParameterExpression<T> parameter(Class<T> paramClass, String name) {
        return null;
    }

    /**
     * Create a predicate that tests whether a collection is empty.
     *
     * @param collection expression
     * @return is-empty predicate
     */
    @Override
    public <C extends Collection<?>> Predicate isEmpty(Expression<C> collection) {
        return null;
    }

    /**
     * Create a predicate that tests whether a collection is
     * not empty.
     *
     * @param collection expression
     * @return is-not-empty predicate
     */
    @Override
    public <C extends Collection<?>> Predicate isNotEmpty(Expression<C> collection) {
        return null;
    }

    /**
     * Create an expression that tests the size of a collection.
     *
     * @param collection expression
     * @return size expression
     */
    @Override
    public <C extends Collection<?>> Expression<Integer> size(Expression<C> collection) {
        return null;
    }

    /**
     * Create an expression that tests the size of a collection.
     *
     * @param collection collection
     * @return size expression
     */
    @Override
    public <C extends Collection<?>> Expression<Integer> size(C collection) {
        return null;
    }

    /**
     * Create a predicate that tests whether an element is
     * a member of a collection.
     * If the collection is empty, the predicate will be false.
     *
     * @param elem       element expression
     * @param collection expression
     * @return is-member predicate
     */
    @Override
    public <E, C extends Collection<E>> Predicate isMember(Expression<E> elem, Expression<C> collection) {
        return null;
    }

    /**
     * Create a predicate that tests whether an element is
     * a member of a collection.
     * If the collection is empty, the predicate will be false.
     *
     * @param elem       element
     * @param collection expression
     * @return is-member predicate
     */
    @Override
    public <E, C extends Collection<E>> Predicate isMember(E elem, Expression<C> collection) {
        return null;
    }

    /**
     * Create a predicate that tests whether an element is
     * not a member of a collection.
     * If the collection is empty, the predicate will be true.
     *
     * @param elem       element expression
     * @param collection expression
     * @return is-not-member predicate
     */
    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(Expression<E> elem, Expression<C> collection) {
        return null;
    }

    /**
     * Create a predicate that tests whether an element is
     * not a member of a collection.
     * If the collection is empty, the predicate will be true.
     *
     * @param elem       element
     * @param collection expression
     * @return is-not-member predicate
     */
    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(E elem, Expression<C> collection) {
        return null;
    }

    /**
     * Create an expression that returns the values of a map.
     *
     * @param map map
     * @return collection expression
     */
    @Override
    public <V, M extends Map<?, V>> Expression<Collection<V>> values(M map) {
        return null;
    }

    /**
     * Create an expression that returns the keys of a map.
     *
     * @param map map
     * @return set expression
     */
    @Override
    public <K, M extends Map<K, ?>> Expression<Set<K>> keys(M map) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     *
     * @param x       string expression
     * @param pattern string expression
     * @return like predicate
     */
    @Override
    public Predicate like(Expression<String> x, Expression<String> pattern) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     *
     * @param x       string expression
     * @param pattern string
     * @return like predicate
     */
    @Override
    public Predicate like(Expression<String> x, String pattern) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     *
     * @param x          string expression
     * @param pattern    string expression
     * @param escapeChar escape character expression
     * @return like predicate
     */
    @Override
    public Predicate like(Expression<String> x, Expression<String> pattern, Expression<Character> escapeChar) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     *
     * @param x          string expression
     * @param pattern    string expression
     * @param escapeChar escape character
     * @return like predicate
     */
    @Override
    public Predicate like(Expression<String> x, Expression<String> pattern, char escapeChar) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     *
     * @param x          string expression
     * @param pattern    string
     * @param escapeChar escape character expression
     * @return like predicate
     */
    @Override
    public Predicate like(Expression<String> x, String pattern, Expression<Character> escapeChar) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     *
     * @param x          string expression
     * @param pattern    string
     * @param escapeChar escape character
     * @return like predicate
     */
    @Override
    public Predicate like(Expression<String> x, String pattern, char escapeChar) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     *
     * @param x       string expression
     * @param pattern string expression
     * @return not-like predicate
     */
    @Override
    public Predicate notLike(Expression<String> x, Expression<String> pattern) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     *
     * @param x       string expression
     * @param pattern string
     * @return not-like predicate
     */
    @Override
    public Predicate notLike(Expression<String> x, String pattern) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     *
     * @param x          string expression
     * @param pattern    string expression
     * @param escapeChar escape character expression
     * @return not-like predicate
     */
    @Override
    public Predicate notLike(Expression<String> x, Expression<String> pattern, Expression<Character> escapeChar) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     *
     * @param x          string expression
     * @param pattern    string expression
     * @param escapeChar escape character
     * @return not-like predicate
     */
    @Override
    public Predicate notLike(Expression<String> x, Expression<String> pattern, char escapeChar) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     *
     * @param x          string expression
     * @param pattern    string
     * @param escapeChar escape character expression
     * @return not-like predicate
     */
    @Override
    public Predicate notLike(Expression<String> x, String pattern, Expression<Character> escapeChar) {
        return null;
    }

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     *
     * @param x          string expression
     * @param pattern    string
     * @param escapeChar escape character
     * @return not-like predicate
     */
    @Override
    public Predicate notLike(Expression<String> x, String pattern, char escapeChar) {
        return null;
    }

    /**
     * Create an expression for string concatenation.
     *
     * @param x string expression
     * @param y string expression
     * @return expression corresponding to concatenation
     */
    @Override
    public Expression<String> concat(Expression<String> x, Expression<String> y) {
        return null;
    }

    /**
     * Create an expression for string concatenation.
     *
     * @param x string expression
     * @param y string
     * @return expression corresponding to concatenation
     */
    @Override
    public Expression<String> concat(Expression<String> x, String y) {
        return null;
    }

    /**
     * Create an expression for string concatenation.
     *
     * @param x string
     * @param y string expression
     * @return expression corresponding to concatenation
     */
    @Override
    public Expression<String> concat(String x, Expression<String> y) {
        return null;
    }

    /**
     * Create an expression for substring extraction.
     * Extracts a substring starting at the specified position
     * through to end of the string.
     * First position is 1.
     *
     * @param x    string expression
     * @param from start position expression
     * @return expression corresponding to substring extraction
     */
    @Override
    public Expression<String> substring(Expression<String> x, Expression<Integer> from) {
        return null;
    }

    /**
     * Create an expression for substring extraction.
     * Extracts a substring starting at the specified position
     * through to end of the string.
     * First position is 1.
     *
     * @param x    string expression
     * @param from start position
     * @return expression corresponding to substring extraction
     */
    @Override
    public Expression<String> substring(Expression<String> x, int from) {
        return null;
    }

    /**
     * Create an expression for substring extraction.
     * Extracts a substring of given length starting at the
     * specified position.
     * First position is 1.
     *
     * @param x    string expression
     * @param from start position expression
     * @param len  length expression
     * @return expression corresponding to substring extraction
     */
    @Override
    public Expression<String> substring(Expression<String> x, Expression<Integer> from, Expression<Integer> len) {
        return null;
    }

    /**
     * Create an expression for substring extraction.
     * Extracts a substring of given length starting at the
     * specified position.
     * First position is 1.
     *
     * @param x    string expression
     * @param from start position
     * @param len  length
     * @return expression corresponding to substring extraction
     */
    @Override
    public Expression<String> substring(Expression<String> x, int from, int len) {
        return null;
    }

    /**
     * Create expression to trim blanks from both ends of
     * a string.
     *
     * @param x expression for string to trim
     * @return trim expression
     */
    @Override
    public Expression<String> trim(Expression<String> x) {
        return null;
    }

    /**
     * Create expression to trim blanks from a string.
     *
     * @param ts trim specification
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Override
    public Expression<String> trim(Trimspec ts, Expression<String> x) {
        return null;
    }

    /**
     * Create expression to trim character from both ends of
     * a string.
     *
     * @param t expression for character to be trimmed
     * @param x expression for string to trim
     * @return trim expression
     */
    @Override
    public Expression<String> trim(Expression<Character> t, Expression<String> x) {
        return null;
    }

    /**
     * Create expression to trim character from a string.
     *
     * @param ts trim specification
     * @param t  expression for character to be trimmed
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Override
    public Expression<String> trim(Trimspec ts, Expression<Character> t, Expression<String> x) {
        return null;
    }

    /**
     * Create expression to trim character from both ends of
     * a string.
     *
     * @param t character to be trimmed
     * @param x expression for string to trim
     * @return trim expression
     */
    @Override
    public Expression<String> trim(char t, Expression<String> x) {
        return null;
    }

    /**
     * Create expression to trim character from a string.
     *
     * @param ts trim specification
     * @param t  character to be trimmed
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Override
    public Expression<String> trim(Trimspec ts, char t, Expression<String> x) {
        return null;
    }

    /**
     * Create expression for converting a string to lowercase.
     *
     * @param x string expression
     * @return expression to convert to lowercase
     */
    @Override
    public Expression<String> lower(Expression<String> x) {
        return null;
    }

    /**
     * Create expression for converting a string to uppercase.
     *
     * @param x string expression
     * @return expression to convert to uppercase
     */
    @Override
    public Expression<String> upper(Expression<String> x) {
        return null;
    }

    /**
     * Create expression to return length of a string.
     *
     * @param x string expression
     * @return length expression
     */
    @Override
    public Expression<Integer> length(Expression<String> x) {
        return null;
    }

    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     *
     * @param x       expression for string to be searched
     * @param pattern expression for string to be located
     * @return expression corresponding to position
     */
    @Override
    public Expression<Integer> locate(Expression<String> x, Expression<String> pattern) {
        return null;
    }

    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     *
     * @param x       expression for string to be searched
     * @param pattern string to be located
     * @return expression corresponding to position
     */
    @Override
    public Expression<Integer> locate(Expression<String> x, String pattern) {
        return null;
    }

    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     *
     * @param x       expression for string to be searched
     * @param pattern expression for string to be located
     * @param from    expression for position at which to start search
     * @return expression corresponding to position
     */
    @Override
    public Expression<Integer> locate(Expression<String> x, Expression<String> pattern, Expression<Integer> from) {
        return null;
    }

    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     *
     * @param x       expression for string to be searched
     * @param pattern string to be located
     * @param from    position at which to start search
     * @return expression corresponding to position
     */
    @Override
    public Expression<Integer> locate(Expression<String> x, String pattern, int from) {
        return null;
    }

    /**
     * Create expression to return current date.
     *
     * @return expression for current date
     */
    @Override
    public Expression<Date> currentDate() {
        return null;
    }

    /**
     * Create expression to return current timestamp.
     *
     * @return expression for current timestamp
     */
    @Override
    public Expression<Timestamp> currentTimestamp() {
        return null;
    }

    /**
     * Create expression to return current time.
     *
     * @return expression for current time
     */
    @Override
    public Expression<Time> currentTime() {
        return null;
    }

    /**
     * Create predicate to test whether given expression
     * is contained in a list of values.
     *
     * @param expression to be tested against list of values
     * @return in predicate
     */
    @Override
    public <T> In<T> in(Expression<? extends T> expression) {
        return null;
    }

    /**
     * Create an expression that returns null if all its arguments
     * evaluate to null, and the value of the first non-null argument
     * otherwise.
     *
     * @param x expression
     * @param y expression
     * @return coalesce expression
     */
    @Override
    public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Expression<? extends Y> y) {
        return null;
    }

    /**
     * Create an expression that returns null if all its arguments
     * evaluate to null, and the value of the first non-null argument
     * otherwise.
     *
     * @param x expression
     * @param y value
     * @return coalesce expression
     */
    @Override
    public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Y y) {
        return null;
    }

    /**
     * Create an expression that tests whether its argument are
     * equal, returning null if they are and the value of the
     * first expression if they are not.
     *
     * @param x expression
     * @param y expression
     * @return nullif expression
     */
    @Override
    public <Y> Expression<Y> nullif(Expression<Y> x, Expression<?> y) {
        return null;
    }

    /**
     * Create an expression that tests whether its argument are
     * equal, returning null if they are and the value of the
     * first expression if they are not.
     *
     * @param x expression
     * @param y value
     * @return nullif expression
     */
    @Override
    public <Y> Expression<Y> nullif(Expression<Y> x, Y y) {
        return null;
    }

    /**
     * Create a coalesce expression.
     *
     * @return coalesce expression
     */
    @Override
    public <T> Coalesce<T> coalesce() {
        return null;
    }

    /**
     * Create a simple case expression.
     *
     * @param expression to be tested against the case conditions
     * @return simple case expression
     */
    @Override
    public <C, R> SimpleCase<C, R> selectCase(Expression<? extends C> expression) {
        return null;
    }

    /**
     * Create a general case expression.
     *
     * @return general case expression
     */
    @Override
    public <R> Case<R> selectCase() {
        return null;
    }

    /**
     * Create an expression for the execution of a database
     * function.
     *
     * @param name function name
     * @param type expected result type
     * @param args function arguments
     * @return expression
     */
    @Override
    public <T> Expression<T> function(String name, Class<T> type, Expression<?>... args) {
        return null;
    }

    /**
     * Downcast Join object to the specified type.
     *
     * @param join Join object
     * @param type type to be downcast to
     * @return Join object of the specified type
     * @since Java Persistence 2.1
     */
    @Override
    public <X, T, V extends T> Join<X, V> treat(Join<X, T> join, Class<V> type) {
        return null;
    }

    /**
     * Downcast CollectionJoin object to the specified type.
     *
     * @param join CollectionJoin object
     * @param type type to be downcast to
     * @return CollectionJoin object of the specified type
     * @since Java Persistence 2.1
     */
    @Override
    public <X, T, E extends T> CollectionJoin<X, E> treat(CollectionJoin<X, T> join, Class<E> type) {
        return null;
    }

    /**
     * Downcast SetJoin object to the specified type.
     *
     * @param join SetJoin object
     * @param type type to be downcast to
     * @return SetJoin object of the specified type
     * @since Java Persistence 2.1
     */
    @Override
    public <X, T, E extends T> SetJoin<X, E> treat(SetJoin<X, T> join, Class<E> type) {
        return null;
    }

    /**
     * Downcast ListJoin object to the specified type.
     *
     * @param join ListJoin object
     * @param type type to be downcast to
     * @return ListJoin object of the specified type
     * @since Java Persistence 2.1
     */
    @Override
    public <X, T, E extends T> ListJoin<X, E> treat(ListJoin<X, T> join, Class<E> type) {
        return null;
    }

    /**
     * Downcast MapJoin object to the specified type.
     *
     * @param join MapJoin object
     * @param type type to be downcast to
     * @return MapJoin object of the specified type
     * @since Java Persistence 2.1
     */
    @Override
    public <X, K, T, V extends T> MapJoin<X, K, V> treat(MapJoin<X, K, T> join, Class<V> type) {
        return null;
    }

    /**
     * Downcast Path object to the specified type.
     *
     * @param path path
     * @param type type to be downcast to
     * @return Path object of the specified type
     * @since Java Persistence 2.1
     */
    @Override
    public <X, T extends X> Path<T> treat(Path<X> path, Class<T> type) {
        return null;
    }

    /**
     * Downcast Root object to the specified type.
     *
     * @param root root
     * @param type type to be downcast to
     * @return Root object of the specified type
     * @since Java Persistence 2.1
     */
    @Override
    public <X, T extends X> Root<T> treat(Root<X> root, Class<T> type) {
        return null;
    }
}
