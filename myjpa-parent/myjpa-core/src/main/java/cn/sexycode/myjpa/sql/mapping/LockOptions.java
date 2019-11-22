package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.mapping.LockMode;

import java.io.Serializable;
import java.util.*;

/**
 * Contains locking details (LockMode, Timeout and Scope).
 */
public class LockOptions implements Serializable {
    /**
     * Represents LockMode.NONE (timeout + scope do not apply).
     */
    public static final LockOptions NONE = new LockOptions(cn.sexycode.myjpa.sql.mapping.LockMode.NONE);

    /**
     * Represents LockMode.READ (timeout + scope do not apply).
     */
    public static final LockOptions READ = new LockOptions(cn.sexycode.myjpa.sql.mapping.LockMode.READ);

    /**
     * Represents LockMode.UPGRADE (will wait forever for lock and scope of false meaning only entity is locked).
     */
    @SuppressWarnings("deprecation")
    public static final LockOptions UPGRADE = new LockOptions(cn.sexycode.myjpa.sql.mapping.LockMode.UPGRADE);

    /**
     * Indicates that the database should not wait at all to acquire the pessimistic lock.
     *
     * @see #getTimeOut
     */
    public static final int NO_WAIT = 0;

    /**
     * Indicates that there is no timeout for the acquisition.
     *
     * @see #getTimeOut
     */
    public static final int WAIT_FOREVER = -1;

    /**
     * Indicates that rows that are already locked should be skipped.
     *
     * @see #getTimeOut()
     */
    public static final int SKIP_LOCKED = -2;

    private cn.sexycode.myjpa.sql.mapping.LockMode lockMode = cn.sexycode.myjpa.sql.mapping.LockMode.NONE;

    private int timeout = WAIT_FOREVER;

    private Map<String, cn.sexycode.myjpa.sql.mapping.LockMode> aliasSpecificLockModes;

    private Boolean followOnLocking;

    /**
     * Constructs a LockOptions with all default options.
     */
    public LockOptions() {
    }

    /**
     * Constructs a LockOptions with the given lock mode.
     *
     * @param lockMode The lock mode to use
     */
    public LockOptions(cn.sexycode.myjpa.sql.mapping.LockMode lockMode) {
        this.lockMode = lockMode;
    }

    /**
     * Retrieve the overall lock mode in effect for this set of options.
     * <p/>
     * In certain contexts (hql and criteria), lock-modes can be defined in an
     * even more granular {@link #setAliasSpecificLockMode(String, cn.sexycode.myjpa.sql.mapping.LockMode) per-alias} fashion
     *
     * @return The overall lock mode.
     */
    public cn.sexycode.myjpa.sql.mapping.LockMode getLockMode() {
        return lockMode;
    }

    /**
     * Set the overall {@link cn.sexycode.myjpa.sql.mapping.LockMode} to be used.  The default is
     * {@link cn.sexycode.myjpa.sql.mapping.LockMode#NONE}
     *
     * @param lockMode The new overall lock mode to use.
     * @return this (for method chaining).
     */
    public LockOptions setLockMode(cn.sexycode.myjpa.sql.mapping.LockMode lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    /**
     * Specify the {@link cn.sexycode.myjpa.sql.mapping.LockMode} to be used for a specific query alias.
     *
     * @param alias    used to reference the LockMode.
     * @param lockMode The lock mode to apply to the given alias
     * @return this LockRequest instance for operation chaining.
     */
    public LockOptions setAliasSpecificLockMode(String alias, cn.sexycode.myjpa.sql.mapping.LockMode lockMode) {
        if (aliasSpecificLockModes == null) {
            aliasSpecificLockModes = new LinkedHashMap<>();
        }
        aliasSpecificLockModes.put(alias, lockMode);
        return this;
    }

    /**
     * Get the {@link cn.sexycode.myjpa.sql.mapping.LockMode} explicitly specified for the given alias via
     * {@link #setAliasSpecificLockMode}
     * <p/>
     * Differs from {@link #getEffectiveLockMode} in that here we only return
     * explicitly specified alias-specific lock modes.
     *
     * @param alias The alias for which to locate the explicit lock mode.
     * @return The explicit lock mode for that alias.
     */
    public cn.sexycode.myjpa.sql.mapping.LockMode getAliasSpecificLockMode(String alias) {
        if (aliasSpecificLockModes == null) {
            return null;
        }
        return aliasSpecificLockModes.get(alias);
    }

    /**
     * Determine the {@link cn.sexycode.myjpa.sql.mapping.LockMode} to apply to the given alias.  If no
     * mode was explicitly {@link #setAliasSpecificLockMode set}, the
     * {@link #getLockMode overall mode} is returned.  If the overall lock mode is
     * <tt>null</tt> as well, {@link cn.sexycode.myjpa.sql.mapping.LockMode#NONE} is returned.
     * <p/>
     * Differs from {@link #getAliasSpecificLockMode} in that here we fallback to we only return
     * the overall lock mode.
     *
     * @param alias The alias for which to locate the effective lock mode.
     * @return The effective lock mode.
     */
    public cn.sexycode.myjpa.sql.mapping.LockMode getEffectiveLockMode(String alias) {
        cn.sexycode.myjpa.sql.mapping.LockMode lockMode = getAliasSpecificLockMode(alias);
        if (lockMode == null) {
            lockMode = this.lockMode;
        }
        return lockMode == null ? cn.sexycode.myjpa.sql.mapping.LockMode.NONE : lockMode;
    }

    /**
     * Does this LockOptions object define alias-specific lock modes?
     *
     * @return {@code true} if this LockOptions object define alias-specific lock modes; {@code false} otherwise.
     */
    public boolean hasAliasSpecificLockModes() {
        return aliasSpecificLockModes != null && !aliasSpecificLockModes.isEmpty();
    }

    /**
     * Get the number of aliases that have specific lock modes defined.
     *
     * @return the number of explicitly defined alias lock modes.
     */
    public int getAliasLockCount() {
        if (aliasSpecificLockModes == null) {
            return 0;
        }
        return aliasSpecificLockModes.size();
    }

    /**
     * Iterator for accessing Alias (key) and LockMode (value) as Map.Entry.
     *
     * @return Iterator for accessing the Map.Entry's
     */
    public Iterator<Map.Entry<String, cn.sexycode.myjpa.sql.mapping.LockMode>> getAliasLockIterator() {
        return getAliasSpecificLocks().iterator();
    }

    /**
     * Iterable access to alias (key) and LockMode (value) as Map.Entry.
     *
     * @return Iterable for accessing the Map.Entry's
     */
    public Iterable<Map.Entry<String, cn.sexycode.myjpa.sql.mapping.LockMode>> getAliasSpecificLocks() {
        if (aliasSpecificLockModes == null) {
            return Collections.emptyList();
        }
        return aliasSpecificLockModes.entrySet();
    }

    /**
     * Currently needed for follow-on locking.
     *
     * @return The greatest of all requested lock modes.
     */
    public cn.sexycode.myjpa.sql.mapping.LockMode findGreatestLockMode() {
        cn.sexycode.myjpa.sql.mapping.LockMode lockModeToUse = getLockMode();
        if (lockModeToUse == null) {
            lockModeToUse = cn.sexycode.myjpa.sql.mapping.LockMode.NONE;
        }

        if (aliasSpecificLockModes == null) {
            return lockModeToUse;
        }

        for (cn.sexycode.myjpa.sql.mapping.LockMode lockMode : aliasSpecificLockModes.values()) {
            if (lockMode.greaterThan(lockModeToUse)) {
                lockModeToUse = lockMode;
            }
        }

        return lockModeToUse;
    }

    /**
     * Retrieve the current timeout setting.
     * <p/>
     * The timeout is the amount of time, in milliseconds, we should instruct the database
     * to wait for any requested pessimistic lock acquisition.
     * <p/>
     * {@link #NO_WAIT}, {@link #WAIT_FOREVER} or {@link #SKIP_LOCKED} represent 3 "magic" values.
     *
     * @return timeout in milliseconds, {@link #NO_WAIT}, {@link #WAIT_FOREVER} or {@link #SKIP_LOCKED}
     */
    public int getTimeOut() {
        return timeout;
    }

    /**
     * Set the timeout setting.
     * <p/>
     * See {@link #getTimeOut} for a discussion of meaning.
     *
     * @param timeout The new timeout setting.
     * @return this (for method chaining).
     * @see #getTimeOut
     */
    public LockOptions setTimeOut(int timeout) {
        this.timeout = timeout;
        return this;
    }

    private boolean scope;

    /**
     * Retrieve the current lock scope setting.
     * <p/>
     * "scope" is a JPA defined term.  It is basically a cascading of the lock to associations.
     *
     * @return true if locking will be extended to owned associations
     */
    public boolean getScope() {
        return scope;
    }

    /**
     * Set the scope.
     *
     * @param scope The new scope setting
     * @return this (for method chaining).
     */
    public LockOptions setScope(boolean scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Retrieve the current follow-on-locking setting.
     *
     * @return true if follow-on-locking is enabled
     */
    public Boolean getFollowOnLocking() {
        return followOnLocking;
    }

    /**
     * Set the the follow-on-locking setting.
     *
     * @param followOnLocking The new follow-on-locking setting
     * @return this (for method chaining).
     */
    public LockOptions setFollowOnLocking(Boolean followOnLocking) {
        this.followOnLocking = followOnLocking;
        return this;
    }

    /**
     * Make a copy.
     *
     * @return The copy
     */
    public LockOptions makeCopy() {
        final LockOptions copy = new LockOptions();
        copy(this, copy);
        return copy;
    }

    /**
     * Perform a shallow copy.
     *
     * @param source      Source for the copy (copied from)
     * @param destination Destination for the copy (copied to)
     * @return destination
     */
    public static LockOptions copy(LockOptions source, LockOptions destination) {
        destination.setLockMode(source.getLockMode());
        destination.setScope(source.getScope());
        destination.setTimeOut(source.getTimeOut());
        if (source.aliasSpecificLockModes != null) {
            destination.aliasSpecificLockModes = new HashMap<String, LockMode>(source.aliasSpecificLockModes);
        }
        destination.setFollowOnLocking(source.getFollowOnLocking());
        return destination;
    }
}
