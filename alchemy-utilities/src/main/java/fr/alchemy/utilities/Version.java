package fr.alchemy.utilities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <code>Version</code> represents a sequence-based software versioning scheme which only allow the use of numbers. A qualifier
 * can however be defined using the provided {@link DevelopmentStage}.
 * <p>
 * The parsed identifiers string must match the following template:
 * <li>"major.minor.patch", where the major, minor and patch constants must be numbers in range of 0 to {@link Integer#MAX_VALUE}.</li>
 * <p>
 * However there is no limitation in how much identifier numbers can be append, it is usually up to 3 or 2 if there is no patch. 
 * 
 * @author GnosticOccultist
 */
public final class Version implements Comparable<Version>, Cloneable, Serializable {
	
	private static final long serialVersionUID = -3609359545455861325L;

	/**
	 * The indices for the 3 commonly found version identifiers (0&rarr;MAJOR, 1&rarr;MINOR, 2&rarr;PATCH).
	 */
	public static final int MAJOR_INDEX = 0, MINOR_INDEX = 1, PATCH_INDEX = 2;

	/**
	 * The development stage qualifier.
	 */
	private final DevelopmentStage qualifier;
	/**
	 * The sequence-based identifiers of the version.
	 */
	private final int[] identifiers;
	
	/**
	 * Instantiates a new <code>Version</code> with the given string representing the version identifiers
	 * seperated with a dot.
	 * The qualifier of the version is set to {@link DevelopmentStage#STABLE}.
	 * 
	 * @param version The version string to parse (not null, not empty).
	 */
	public Version(String version) {
		this(DevelopmentStage.STABLE, version);
	}
	
	/**
	 * Instantiates a new <code>Version</code> with the given {@link DevelopmentStage} and string representing 
	 * the version identifiers seperated with a dot.
	 * 
	 * @param qualifier The development stage of the version to use as a prefix (not null).
	 * @param version   The version string to parse (not null, not empty).
	 */
	public Version(DevelopmentStage qualifier, String version) {
		Validator.nonNull(qualifier, "The version qualifier can't be null!");
		this.qualifier = qualifier;
		this.identifiers = parse(version);
	}
	
	/**
	 * Instantiates a new <code>Version</code> with the given array of version identifiers.
	 * The qualifier of the version is set to {@link DevelopmentStage#STABLE}.
	 * 
	 * @param identifiers The array containing the version identifier numbers (not null, not empty).
	 */
	public Version(int[] identifiers) {
		this(DevelopmentStage.STABLE, identifiers);
	}

	/**
	 * Instantiates a new <code>Version</code> with the given {@link DevelopmentStage} and array of 
	 * version identifiers.
	 * 
	 * @param qualifier   The development stage of the version to use as a prefix (not null).
	 * @param identifiers The array containing the version identifier numbers (not null, not empty).
	 */
	public Version(DevelopmentStage qualifier, int[] identifiers) {
		Validator.nonNull(qualifier, "The version qualifier can't be null!");
		Validator.nonEmpty(identifiers, "The version identifiers can't be empty or null!");
		this.qualifier = qualifier;
		this.identifiers = identifiers;
	}
	
	/**
	 * Parses the given string version to an array of identifier numbers to represent the <code>Version</code>.
	 * 
	 * @param version The version string to parse (not null, not empty).
	 * @return		  An array containing the parsed version identifier numbers (not null).
	 */
	private int[] parse(String version) {
		Validator.nonEmpty(version, "The version string can't be empty or null!");
		return Stream.of(version.split("\\."))
				.mapToInt(Integer::parseInt).toArray();
	}
	
	/**
	 * Return the major identifier number of the <code>Version</code>.
	 * 
	 * @return The major identifier of the version (&ge;0).
	 */
	public int major() {
		return identifiers[MAJOR_INDEX];
	}
	
	/**
	 * Return the minor identifier number of the <code>Version</code>.
	 * 
	 * @return The minor identifier of the version (&ge;0).
	 */
	public int minor() {
		return identifiers[MINOR_INDEX];
	}
	
	/**
	 * Return the patch identifier number of the <code>Version</code>.
	 * 
	 * @return The patch identifier of the version (&ge;0).
	 */
	public int patch() {
		return identifiers.length > 2 ? identifiers[PATCH_INDEX] : 0;
	}
	
	@Override
	public int compareTo(Version other) {
		/*
		 * Check qualifier level first.
		 */
		if(other.qualifier != qualifier) {
			return (int) Math.signum(qualifier.ordinal() - other.qualifier.ordinal());
		}
		
		/*
		 * Secondly check for identifier differences.
		 */
		int[] otherIdentifiers = other.identifiers;
		int commonIdentifiers = Math.min(otherIdentifiers.length, identifiers.length);
		for(int i = 0; i < commonIdentifiers; i++) {
			if(identifiers[i] != otherIdentifiers[i]) {
				return (int) Math.signum(identifiers[i] - otherIdentifiers[i]);
			}
		}
		
		/*
		 * Finally check for identifiers length.
		 */
		return identifiers.length - otherIdentifiers.length;
	}
	
	@Override
	protected Version clone(){
		return new Version(qualifier, identifiers);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(qualifier, identifiers);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Version)) {
			return false;
		}
		
		Version other = (Version) obj;
		return qualifier.equals(other.qualifier) && Arrays.equals(identifiers, other.identifiers);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(qualifier.name().toLowerCase() + " - ");
		for(int i = 0; i < identifiers.length; i++) {
			sb.append(identifiers[i]);
			if(i != identifiers.length - 1) {
				sb.append(".");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * <code>DevelopmentStage</code> enumerates the major development stages that a software goes through, the final being 
	 * {@value #STABLE}.
	 * 
	 * @author GnosticOccultist
	 */
	public enum DevelopmentStage {
		/**
		 * A version stage where activities are performed before formal testing of the software (aka nightly or dev release).
		 */
		PRE_ALPHA,
		/**
		 * A version stage where software testing begins, so it can contain serious erros or not yet fully implemented functionalities.
		 */
		ALPHA,
		/**
		 * A version stage following the alpha one where software is feature complete but likely to contain known or unknown bugs, performance
		 * or speed issues and may still cause crash or data loss.
		 */
		BETA,
		/**
		 * A beta version stage with potential to be a stable product, which is ready to release unless significant bugs emerge (aka release candidate).
		 */
		PRE_RELEASE,
		/**
		 * A pre-release version stage, also called production release, which has passed all verifications / tests. The remaining bugs are considered as acceptable,
		 * and so the release can go to production.
		 */
		STABLE;
	}
}
