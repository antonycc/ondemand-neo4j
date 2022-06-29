package uk.co.polycode.neo4j;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * PostalAddress
 *
 * The mailing address.
 *
 *
 *             This file was generated by OWL to Java as a transformation of the Schema.org schema Version 14.0.
 *             Schema.org is released under the Creative Commons Attribution-ShareAlike License (version 3.0).
 *             The Schema.org license is applicable to the generated source files and the license is available from
 *             <a href="https://creativecommons.org/licenses/by-sa/3.0/">...</a>
 *
 */
@Node
public class PostalAddress { // extends Thing {

	/**
	 * The synthetic key for this item.
	 */
	@Id
	public String id;

	/**
	 * A PostalAddress is a Thing
	 */
	public Thing thing;

	/**
	 * The country. For example, USA. You can also provide the two-letter <a href="http://en.wikipedia.org/wiki/ISO_3166-1">ISO 3166-1 alpha-2 country code</a>.
	 */
	public String addressCountry;

	/**
	 * The locality in which the street address is, and which is in the region. For example, Mountain View.
	 */
	public String addressLocality;

	/**
	 * The region in which the locality is, and which is in the country. For example, California or another appropriate first-level <a href="https://en.wikipedia.org/wiki/List_of_administrative_divisions_by_country">Administrative division</a>
	 */
	public String addressRegion;

	/**
	 * The post office box number for PO box addresses.
	 */
	public String postOfficeBoxNumber;

	/**
	 * The postal code. For example, 94043.
	 */
	public String postalCode;

	/**
	 * The street address. For example, 1600 Amphitheatre Pkwy.
	 */
	public String streetAddress;

	/**
	 * Where to find the definition of the OWL Class used to generate this Java class.
	 */
	public String isDefinedBy = "https://schema.org/PostalAddress";
}
