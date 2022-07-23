package uk.co.polycode.neo4j;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.net.URL;
import java.util.UUID;

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
	@GeneratedValue // TODO: generate UUID and check best version to use. (generatorRef = "uuid")
	public UUID id;

	/**
	 * An additional type for the item, typically used for adding more specific types from external vocabularies in microdata syntax. This is a relationship between something and a class that the thing is in. In RDFa syntax, it is better to use the native RDFa syntax - the 'typeof' attribute - for multiple types. Schema.org tools may have only weaker understanding of extra types, in particular those defined externally.
	 * (From Thing)
	 */
	@Property(name="additionalType")
	public String additionalType;

	/**
	 * An alias for the item.
	 * (From Thing)
	 */
	@Property(name="alternateName")
	public String alternateName;

	/**
	 * A description of the item.
	 * (From Thing)
	 */
	@Property(name="description")
	public String description;

	/**
	 * A sub property of description. A short description of the item used to disambiguate from other, similar items. Information from other properties (in particular, name) may be necessary for the description to be useful for disambiguation.
	 * (From Thing)
	 */
	@Property(name="disambiguatingDescription")
	public String disambiguatingDescription;

	/**
	 * The identifier property represents any kind of identifier for any kind of <a class="localLink" href="https://schema.org/Thing">Thing</a>, such as ISBNs, GTIN codes, UUIDs etc. Schema.org provides dedicated properties for representing many of these, either as textual strings or as URL (URI) links. See <a href="/docs/datamodel.html#identifierBg">background notes</a> for more details.
	 * (From Thing)
	 */
	@Property(name="identifier")
	public String identifier;

	/**
	 * An image of the item. This can be a <a class="localLink" href="https://schema.org/URL">URL</a> or a fully described <a class="localLink" href="https://schema.org/ImageObject">ImageObject</a>.
	 * (From Thing)
	 */
	@Property(name="image")
	public String image;

	/**
	 * The name of the item.
	 * (From Thing)
	 */
	@Property(name="name")
	public String name;

	/**
	 * URL of a reference Web page that unambiguously indicates the item's identity. E.g. the URL of the item's Wikipedia page, Wikidata entry, or official website.
	 * (From Thing)
	 */
	@Property(name="sameAs")
	public String sameAs;

	/**
	 * URL of the item.
	 * (From Thing)
	 */
	@Property(name="url")
	public URL url;

	/**
	 * The country. For example, USA. You can also provide the two-letter <a href="http://en.wikipedia.org/wiki/ISO_3166-1">ISO 3166-1 alpha-2 country code</a>.
	 */
	@Property(name="addressCountry")
	public String addressCountry;

	/**
	 * The locality in which the street address is, and which is in the region. For example, Mountain View.
	 */
	@Property(name="addressLocality")
	public String addressLocality;

	/**
	 * The region in which the locality is, and which is in the country. For example, California or another appropriate first-level <a href="https://en.wikipedia.org/wiki/List_of_administrative_divisions_by_country">Administrative division</a>
	 */
	@Property(name="addressRegion")
	public String addressRegion;

	/**
	 * The post office box number for PO box addresses.
	 */
	@Property(name="postOfficeBoxNumber")
	public String postOfficeBoxNumber;

	/**
	 * The postal code. For example, 94043.
	 */
	@Property(name="postalCode")
	public String postalCode;

	/**
	 * The street address. For example, 1600 Amphitheatre Pkwy.
	 */
	@Property(name="streetAddress")
	public String streetAddress;

	/**
	 * Where to find the definition of the OWL Class used to generate this Java class.
	 */
	@Property(name="isDefinedBy")
	public static String isDefinedBy = "https://schema.org/PostalAddress";
}

