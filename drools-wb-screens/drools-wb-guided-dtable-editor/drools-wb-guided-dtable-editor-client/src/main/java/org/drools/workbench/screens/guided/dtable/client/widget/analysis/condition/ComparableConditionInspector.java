/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.condition;

import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.checks.util.Covers;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.checks.util.IsSubsuming;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.checks.util.Operator;

public class ComparableConditionInspector<T extends Comparable<T>>
        extends ConditionInspector
        implements Covers<T> {

    protected final Operator operator;
    protected final T value;

    public ComparableConditionInspector( final Pattern52 pattern,
                                         final String factField,
                                         final T value,
                                         final String operator ) {
        super( pattern,
               factField );

        this.operator = Operator.resolve( operator );

        this.value = value;

    }

    public T getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public boolean conflicts( Object other ) {
        if ( other instanceof ComparableConditionInspector ) {
            switch ( ( (ComparableConditionInspector) other ).getOperator() ) {
                case NOT_EQUALS:
                    switch ( operator ) {
                        case NOT_EQUALS:
                            return false;
                    }
                default:
                    boolean overlaps = overlaps( other );
                    boolean overlapsNOT = !overlaps;
                    return overlapsNOT;
            }
        }
        return false;
    }

    @Override
    public boolean isRedundant( Object object ) {
        if ( object instanceof IsSubsuming ) {
            return subsumes( object ) && ((IsSubsuming) object).subsumes( this );
        } else {
            return false;
        }
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    public boolean overlaps( Object other ) {
        if ( other instanceof ComparableConditionInspector ) {

            ComparableConditionInspector<T> anotherPoint = (ComparableConditionInspector) other;

            if ( anotherPoint != null ) {
                switch ( anotherPoint.getOperator() ) {
                    case NOT_EQUALS:
                        switch (operator) {
                            case EQUALS:
                                return !getValue().equals( anotherPoint.getValue() );
                            default:
                                return true;
                        }
                    case EQUALS:
                        switch (operator) {
                            case NOT_EQUALS:
                                return !getValue().equals( anotherPoint.getValue() );
                            default:
                                return covers( anotherPoint.getValue() );
                        }
                    case GREATER_THAN_OR_EQUALS:
                        switch (operator) {
                            case NOT_EQUALS:
                                return true;
                            case LESS_THAN:
                            case BEFORE:
                                return anotherPoint.covers( getValue() )
                                        && covers( anotherPoint.getValue() );
                            default:
                                return covers( anotherPoint.getValue() )
                                        || anotherPoint.covers( getValue() );
                        }
                    case LESS_THAN_OR_EQUALS:
                        switch ( operator ) {
                            case NOT_EQUALS:
                                return true;
                            case GREATER_THAN:
                            case AFTER:
                                return anotherPoint.covers( getValue() )
                                        && covers( anotherPoint.getValue() );
                            default:
                                return covers( anotherPoint.getValue() )
                                        || anotherPoint.covers( getValue() );
                        }
                    case LESS_THAN:
                    case BEFORE:
                        switch ( operator ) {
                            case EQUALS:
                                return anotherPoint.covers( getValue() );
                            case NOT_EQUALS:
                                return true;
                            case LESS_THAN:
                            case BEFORE:
                                return valueIsEqualTo( anotherPoint.getValue() )
                                        || anotherPoint.covers( getValue() )
                                        || covers( anotherPoint.getValue() );
                            case GREATER_THAN_OR_EQUALS:
                                return anotherPoint.covers( getValue() )
                                        && covers( anotherPoint.getValue() );
                            default:
                                return covers( anotherPoint.getValue() )
                                        || anotherPoint.covers( getValue() );

                        }
                    case GREATER_THAN:
                    case AFTER:
                        switch ( operator ) {
                            case EQUALS:
                                return anotherPoint.covers( getValue() );
                            case NOT_EQUALS:
                                return true;
                            case GREATER_THAN:
                            case AFTER:
                                return valueIsEqualTo( anotherPoint.getValue() )
                                        || anotherPoint.covers( getValue() )
                                        || covers( anotherPoint.getValue() );
                            case LESS_THAN_OR_EQUALS:
                                return anotherPoint.covers( getValue() )
                                        && covers( anotherPoint.getValue() );
                            default:
                                return covers( anotherPoint.getValue() )
                                        || anotherPoint.covers( getValue() );
                        }
                    default:
                        return false;
                }
            }
        }

        return false;
    }

    @Override
    public boolean subsumes( Object other ) {
        if ( other instanceof ComparableConditionInspector ) {

            ComparableConditionInspector anotherPoint = (ComparableConditionInspector) other;

            if ( anotherPoint != null ) {
                switch ( anotherPoint.getOperator() ) {
                    case NOT_EQUALS:
                        switch (operator) {
                            case NOT_EQUALS:
                                return getValue().equals( anotherPoint.getValue() );
                            case EQUALS:
                                boolean valueIsEqualTo = valueIsEqualTo( anotherPoint.getValue() );
                                boolean covers = covers( anotherPoint.getValue() );
                                return !valueIsEqualTo && !covers;
                            default:
                                return false;
                        }
                    case EQUALS:
                        switch (operator) {
                            case NOT_EQUALS:
                                boolean valueIsEqualTo = valueIsEqualTo( anotherPoint.getValue() );
                                boolean covers = covers( anotherPoint.getValue() );
                                return !valueIsEqualTo && !covers;
                            default:
                                return covers( anotherPoint.getValue() );
                        }
                    case GREATER_THAN_OR_EQUALS:
                        switch ( operator ) {
                            case GREATER_THAN_OR_EQUALS:
                            case GREATER_THAN:
                                return covers( anotherPoint.getValue() );
                            case NOT_EQUALS:
                                return valueIsGreaterThan( anotherPoint.getValue() );
                            default:
                                return false;
                        }

                    case LESS_THAN_OR_EQUALS:
                        switch ( operator ) {
                            case LESS_THAN_OR_EQUALS:
                            case LESS_THAN:
                                return covers( anotherPoint.getValue() );
                            case NOT_EQUALS:
                                return valueIsLessThan( anotherPoint.getValue() );
                            default:
                                return false;
                        }
                    case LESS_THAN:
                        switch ( operator ) {
                            case LESS_THAN_OR_EQUALS:
                                return covers( anotherPoint.getValue() );
                            case LESS_THAN:
                            case NOT_EQUALS:
                                return valueIsLessThanOrEqualTo( anotherPoint.getValue() );
                            default:
                                return false;
                        }
                    case GREATER_THAN:
                        switch ( operator ) {
                            case GREATER_THAN_OR_EQUALS:
                                return covers( anotherPoint.getValue() );
                            case GREATER_THAN:
                            case NOT_EQUALS:
                                return valueIsGreaterThanOrEqualTo( anotherPoint.getValue() );
                            default:
                                return false;

                        }
                    default:
                        return false;
                }
            }
        }

        return false;
    }

    @Override
    public boolean covers( Comparable<T> otherValue ) {
        if ( otherValue instanceof Comparable ) {
            switch ( operator ) {
                case EQUALS:
                    return valueIsEqualTo( otherValue );
                case NOT_EQUALS:
                    return !valueIsEqualTo( otherValue );
                case GREATER_THAN_OR_EQUALS:
                    return valueIsGreaterThanOrEqualTo( otherValue );
                case LESS_THAN_OR_EQUALS:
                    return valueIsLessThanOrEqualTo( otherValue );
                case LESS_THAN:
                case BEFORE:
                    return valueIsLessThan( otherValue );
                case GREATER_THAN:
                case AFTER:
                    return valueIsGreaterThan( otherValue );
                default:
                    return false;
            }

        } else {
            return false;
        }
    }

    private boolean valueIsGreaterThanOrEqualTo( final Comparable<T> otherValue ) {
        return valueIsEqualTo( otherValue ) || valueIsGreaterThan( otherValue );
    }

    private boolean valueIsLessThanOrEqualTo( final Comparable<T> otherValue ) {
        return valueIsEqualTo( otherValue ) || valueIsLessThan( otherValue );
    }

    private boolean valueIsGreaterThan( final Comparable<T> otherValue ) {
        return otherValue.compareTo( getValue() ) > 0;
    }

    private boolean valueIsLessThan( final Comparable<T> otherValue ) {
        return otherValue.compareTo( getValue() ) < 0;
    }

    private boolean valueIsEqualTo( final Comparable<T> otherValue ) {
        return otherValue.compareTo( getValue() ) == 0;
    }

    @Override
    public String toHumanReadableString() {
        return getFactField() + " " + operator + " " + getValue();
    }
}
