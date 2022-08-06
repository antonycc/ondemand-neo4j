package uk.co.polycode.neo4j.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Place
 *
 * Entities that have a somewhat fixed, physical extension.
 *
 *
 *             This file was generated by OWL to Java as a transformation of the Schema.org schema Version 14.0.
 *             Schema.org is released under the Creative Commons Attribution-ShareAlike License (version 3.0).
 *             The Schema.org license is applicable to the generated source files and the license is available from
 *             <a href="https://creativecommons.org/licenses/by-sa/3.0/">...</a>
 *
 */
@Node
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Place { // extends Thing {

	/**
	 * The synthetic key for this item.
	 */
	@Id
	@GeneratedValue // TODO: generate UUID and check best version to use. (generatorRef = "uuid")
	@Property(name="id")
	//@Value("#{target.id}")
	public UUID id;

	/**
	 * A description of the item.
	 * (From Thing)
	 */
	public String description;

	/**
	 * The identifier property represents any kind of identifier for any kind of <a class="localLink" href="https://schema.org/Thing">Thing</a>, such as ISBNs, GTIN codes, UUIDs etc. Schema.org provides dedicated properties for representing many of these, either as textual strings or as URL (URI) links. See <a href="/docs/datamodel.html#identifierBg">background notes</a> for more details.
	 * (From Thing)
	 */
	public String identifier;

	/**
	 * An image of the item. This can be a <a class="localLink" href="https://schema.org/URL">URL</a> or a fully described <a class="localLink" href="https://schema.org/ImageObject">ImageObject</a>.
	 * (From Thing)
	 */
	public String image;

	/**
	 * The name of the item.
	 * (From Thing)
	 */
	public String name;

	/**
	 * URL of a reference Web page that unambiguously indicates the item's identity. E.g. the URL of the item's Wikipedia page, Wikidata entry, or official website.
	 * (From Thing)
	 */
	public String sameAs;

	/**
	 * URL of the item.
	 * (From Thing)
	 */
	public URL url;

	/**
	 * The subjective concept of the most famous person associated with this place.
	 */
	@Relationship(type = "HAS_FAMOUS_PERSON", direction = Relationship.Direction.OUTGOING)
	public List<Person> famousPerson = new ArrayList<>();

	/**
	 * Physical address of the item.
	 */
	public PostalAddress address;

	/**
	 * A short textual code (also called "store code") that uniquely identifies a place of business. The code is typically assigned by the parentOrganization and used in structured URLs.<br/><br/>
	 *
	 * For example, in the URL <a href="http://www.starbucks.co.uk/store-locator/etc/detail/3047">...</a> the code "3047" is a branchCode for a particular branch.
	 */
	public String branchCode;

	/**
	 * The basic containment relation between a place and one that contains it.
	 */
	public Place containedInPlace;

	/**
	 * The basic containment relation between a place and another that it contains.
	 */
	public Place containsPlace;


	/**
	 * A URL to a map of the place.
	 */
	public String hasMap;

	/**
	 * The International Standard of Industrial Classification of All Economic Activities (ISIC), Revision 4 code for a particular organization, business person, or place.
	 */
	public String isicV4;

	/**
	 * Keywords or tags used to describe some item. Multiple textual entries in a keywords list are typically delimited by commas, or by repeating the property.
	 */
	public String keywords;

	/**
	 * The latitude of a location. For example <code>37.42242</code> (<a href="https://en.wikipedia.org/wiki/World_Geodetic_System">WGS 84</a>).
	 */
	public BigDecimal latitude;

	/**
	 * An associated logo.
	 */
	public String logo;

	/**
	 * The longitude of a location. For example <code>-122.08585</code> (<a href="https://en.wikipedia.org/wiki/World_Geodetic_System">WGS 84</a>).
	 */
	public BigDecimal longitude;

	/**
	 * A photograph of this place.
	 */
	public String photoImageObject;

	/**
	 * A photograph of this place.
	 */
	public String photo;

	/**
	 * The telephone number.
	 */
	public String telephone;

	/**
	 * The total number of individuals that may attend an event or venue.
	 */
	public BigInteger maximumAttendeeCapacity;

	/**
	 * A flag to signal that the <a class="localLink" href="https://schema.org/Place">Place</a> is open to public visitors.  If this property is omitted there is no assumed default boolean value
	 */
	public Boolean publicAccess;

	/**
	 * Indicates whether it is allowed to smoke in the place, e.g. in the restaurant, hotel or hotel room.
	 */
	public Boolean smokingAllowed;

	/**
	 * Where to find the definition of the OWL Class used to generate this Java class.
	 */
	public static String isDefinedBy = "https://schema.org/Place";
}
