package uk.co.polycode.neo4j;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.math.BigInteger;
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
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id") // Needed when attributes are nodes
public class Place { // extends Thing {

	/**
	 * The synthetic key for this item.
	 */
	@Id
	@GeneratedValue // TODO: generate UUID and check best version to use. (generatorRef = "uuid")
	public UUID id;

	/**
	 * A Place is a Thing
	 */
	public Thing thing;

	/**
	 * The subjective concept of the most famous person associated with this place.
	 */
	@Relationship(type = "HAS_MOST_FAMOUS_PERSON", direction = Relationship.Direction.OUTGOING)
	public Person mostFamousPerson;

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
	 * The fax number.
	 */
	public String faxNumber;

	/**
	 * Represents a relationship between two geometries (or the places they represent), relating a containing geometry to a contained geometry. "a contains b iff no points of b lie in the exterior of a, and at least one point of the interior of b lies in the interior of a". As defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>.
	 */
	public Place geoContains;

	/**
	 * Represents a relationship between two geometries (or the places they represent), relating a geometry to another that covers it. As defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>.
	 */
	public Place geoCoveredByPlace;

	/**
	 * Represents a relationship between two geometries (or the places they represent), relating a covering geometry to a covered geometry. "Every point of b is a point of (the interior or boundary of) a". As defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>.
	 */
	public Place geoCoversPlace;

	/**
	 * Represents a relationship between two geometries (or the places they represent), relating a geometry to another that crosses it: "a crosses b: they have some but not all interior points in common, and the dimension of the intersection is less than that of at least one of them". As defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>.
	 */
	public Place geoCrossesPlace;

	/**
	 * Represents spatial relations in which two geometries (or the places they represent) are topologically disjoint: they have no point in common. They form a set of disconnected geometries." (a symmetric relationship, as defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>)
	 */
	public Place geoDisjointPlace;

	/**
	 * Represents spatial relations in which two geometries (or the places they represent) are topologically equal, as defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>. "Two geometries are topologically equal if their interiors intersect and no part of the interior or boundary of one geometry intersects the exterior of the other" (a symmetric relationship)
	 */
	public Place geoEquals;

	/**
	 * Represents spatial relations in which two geometries (or the places they represent) have at least one point in common. As defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>.
	 */
	public Place geoIntersectsPlace;

	/**
	 * Represents a relationship between two geometries (or the places they represent), relating a geometry to another that geospatially overlaps it, i.e. they have some but not all points in common. As defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>.
	 */
	public Place geoOverlaps;

	/**
	 * Represents spatial relations in which two geometries (or the places they represent) touch: they have at least one boundary point in common, but no interior points." (a symmetric relationship, as defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a> )
	 */
	public Place geoTouches;

	/**
	 * Represents a relationship between two geometries (or the places they represent), relating a geometry to one that contains it, i.e. it is inside (i.e. within) its interior. As defined in <a href="https://en.wikipedia.org/wiki/DE-9IM">DE-9IM</a>.
	 */
	public Place geoWithin;

	/**
	 * The <a href="http://www.gs1.org/gln">Global Location Number</a> (GLN, sometimes also referred to as International Location Number or ILN) of the respective organization, person, or place. The GLN is a 13-digit number used to identify parties and physical locations.
	 */
	public String globalLocationNumber;

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
	 * A review of the item.
	 */
	public String review;

	/**
	 * A slogan or motto associated with the item.
	 */
	public String slogan;

	/**
	 * The telephone number.
	 */
	public String telephone;

	/**
	 * A page providing information on how to book a tour of some <a class="localLink" href="https://schema.org/Place">Place</a>, such as an <a class="localLink" href="https://schema.org/Accommodation">Accommodation</a> or <a class="localLink" href="https://schema.org/ApartmentComplex">ApartmentComplex</a> in a real estate setting, as well as other kinds of tours as appropriate.
	 */
	public String tourBookingPage;

	/**
	 * Indicates whether some facility (e.g. <a class="localLink" href="https://schema.org/FoodEstablishment">FoodEstablishment</a>, <a class="localLink" href="https://schema.org/CovidTestingFacility">CovidTestingFacility</a>) offers a service that can be used by driving through in a car. In the case of <a class="localLink" href="https://schema.org/CovidTestingFacility">CovidTestingFacility</a> such facilities could potentially help with social distancing from other potentially-infected users.
	 */
	public Boolean hasDriveThroughService;

	/**
	 * A flag to signal that the item, event, or place is accessible for free.
	 */
	public Boolean isAccessibleForFree;

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
	public String isDefinedBy = "https://schema.org/Place";

}

