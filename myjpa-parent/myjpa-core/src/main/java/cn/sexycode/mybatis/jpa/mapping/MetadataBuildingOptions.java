/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;


import cn.sexycode.sql.dialect.function.SQLFunction;
import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;

import javax.persistence.AccessType;
import javax.persistence.SharedCacheMode;
import java.util.Map;

/**
 * Describes the options used while building the Metadata object (during
 * {@link org.hibernate.boot.MetadataBuilder#build()} processing).
 *
 * @author Steve Ebersole
 * @since 5.0
 */
public interface MetadataBuildingOptions {


    /**
     * Access to the environment for scanning.  Consider this temporary; see discussion on
     * {@link ScanEnvironment}
     *
     * @return The scan environment
     */
    ScanEnvironment getScanEnvironment();

    /**
     * Access to the Scanner to be used for scanning.  Can be:<ul>
     * <li>A Scanner instance</li>
     * <li>A Class reference to the Scanner implementor</li>
     * <li>A String naming the Scanner implementor</li>
     * </ul>
     *
     * @return The scanner
     */
    Object getScanner();


    /**
     * Access to the SharedCacheMode for determining whether we should perform second level
     * caching or not.
     *
     * @return The SharedCacheMode
     */
    SharedCacheMode getSharedCacheMode();

    /**
     * Access to any implicit cache AccessType.
     *
     * @return The implicit cache AccessType
     */
    AccessType getImplicitCacheAccessType();


    /**
     * Whether explicit discriminator declarations should be ignored for joined
     * subclass style inheritance.
     *
     * @return {@code true} indicates they should be ignored; {@code false}
     * indicates they should not be ignored.
     * @see org.hibernate.boot.MetadataBuilder#enableExplicitDiscriminatorsForJoinedSubclassSupport
     * @see org.hibernate.cfg.AvailableSettings#IGNORE_EXPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS
     */
    boolean ignoreExplicitDiscriminatorsForJoinedInheritance();

    /**
     * Whether we should do discrimination implicitly joined subclass style inheritance when no
     * discriminator info is provided.
     *
     * @return {@code true} indicates we should do discrimination; {@code false} we should not.
     * @see org.hibernate.boot.MetadataBuilder#enableImplicitDiscriminatorsForJoinedSubclassSupport
     * @see org.hibernate.cfg.AvailableSettings#IMPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS
     */
    boolean createImplicitDiscriminatorsForJoinedInheritance();

    /**
     * Whether we should implicitly force discriminators into SQL selects.  By default,
     * Hibernate will not.  This can be specified per discriminator in the mapping as well.
     *
     * @return {@code true} indicates we should force the discriminator in selects for any mappings
     * which do not say explicitly.
     * @see org.hibernate.cfg.AvailableSettings#FORCE_DISCRIMINATOR_IN_SELECTS_BY_DEFAULT
     */
    boolean shouldImplicitlyForceDiscriminatorInSelect();

    /**
     * Should we use nationalized variants of character data (e.g. NVARCHAR rather than VARCHAR)
     * by default?
     *
     * @return {@code true} if nationalized character data should be used by default; {@code false} otherwise.
     * @see org.hibernate.boot.MetadataBuilder#enableGlobalNationalizedCharacterDataSupport
     * @see org.hibernate.cfg.AvailableSettings#USE_NATIONALIZED_CHARACTER_DATA
     */
    boolean useNationalizedCharacterData();

    boolean isSpecjProprietarySyntaxEnabled();


    /**
     * Access to any SQL functions explicitly registered with the MetadataBuilder.  This
     * does not include Dialect defined functions, etc.
     *
     * @return The SQLFunctions registered through MetadataBuilder
     */
    Map<String, SQLFunction> getSqlFunctions();


//	/**
//	 * Obtain the selected strategy for resolving members identifying persistent attributes
//	 *
//	 * @return The select resolver strategy
//	 */
//	PersistentAttributeMemberResolver getPersistentAttributeMemberResolver();
}
