package uk.co.polycode.neo4j.export;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import uk.co.polycode.neo4j.ExportableFor;
import uk.co.polycode.neo4j.ExportableToListOf;
import uk.co.polycode.neo4j.MappingUtils;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Person
 * A person (alive, dead, undead, or fictional).
 *             This file was generated by OWL to Java as a transformation of the Schema.org schema Version 14.0.
 *             Schema.org is released under the Creative Commons Attribution-ShareAlike License (version 3.0).
 *             The Schema.org license is applicable to the generated source files and the license is available from
 *             <a href="https://creativecommons.org/licenses/by-sa/3.0/">...</a>
 *
 */
// TODO: Attempt to reinstate inheritance from Person
@ExportableFor(clazz=uk.co.polycode.neo4j.data.Person.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Person {

	public static Person from(uk.co.polycode.neo4j.data.Person that) {
		return (Person) MappingUtils.reflectionDeepCopy(that, new Person());
	}

	/**
	 * The synthetic key for this item.
	 */
	public UUID id;

	/**
	 * An alias for the item.
	 * (From Thing)
	 */
	public String alternateName;

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
	 * An additional name for a Person, can be used for a middle name.
	 */
	public String additionalName;

	/**
	 * Physical address of the item.
	 */
	@ExportableToListOf(clazz=PostalAddress.class)
	public List<PostalAddress> address;

	/**
	 * An organization that this person is affiliated with. For example, a school/university, a club, or a team.
	 */
	@ExportableToListOf(clazz=Organization.class)
	public List<Organization> affiliation = new ArrayList<>();

	/**
	 * An organization that the person is an alumni of.
	 */
	@ExportableToListOf(clazz=Organization.class)
	public List<Organization> alumniOf;

	/**
	 * An award won by or for this item.
	 */
	public String award;

	/**
	 * The place where the person was born.
	 */
	@ExportableToListOf(clazz=Place.class)
	public List<Place> birthPlace;

	/**
	 * The brand(s) associated with a product or service, or the brand(s) maintained by an organization or business person.
	 */
	@ExportableToListOf(clazz=Organization.class)
	public List<Organization> brand;

	/**
	 * A child of the person.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> children;

	/**
	 * A colleague of the person.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> colleague;

	/**
	 * The place where the person died.
	 */
	@ExportableToListOf(clazz=Place.class)
	public List<Place> deathPlace;

	/**
	 * Email address.
	 */
	public String email;

	/**
	 * Family name. In the U.S., the last name of a Person.
	 */
	public String familyName;

	/**
	 * The most generic uni-directional social relation.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> follows;

	/**
	 * Gender of something, typically a <a class="localLink" href="https://schema.org/Person">Person</a>, but possibly also fictional characters, animals, etc. While https://schema.org/Male and https://schema.org/Female may be used, text strings are also acceptable for people who do not identify as a binary gender. The <a class="localLink" href="https://schema.org/gender">gender</a> property can also be used in an extended sense to cover e.g. the gender of sports teams. As with the gender of individuals, we do not try to enumerate all possibilities. A mixed-gender <a class="localLink" href="https://schema.org/SportsTeam">SportsTeam</a> can be indicated with a text value of "Mixed".
	 */
	public String gender;

	/**
	 * Given name. In the U.S., the first name of a Person.
	 */
	public String givenName;

	/**
	 * A credential awarded to the Person or Organization.
	 */
	public String hasCredential;

	/**
	 * The Person's occupation. For past professions, use Role for expressing dates.
	 */
	public String hasOccupation;

	/**
	 * The height of the item.
	 */
	public BigInteger height;

	/**
	 * A contact location for a person's residence.
	 */
	@ExportableToListOf(clazz=Place.class)
	public List<Place> homeLocation;

	/**
	 * An honorific prefix preceding a Person's name such as Dr/Mrs/Mr.
	 */
	public String honorificPrefix;

	/**
	 * An honorific suffix following a Person's name such as M.D. /PhD/MSCSW.
	 */
	public String honorificSuffix;

	/**
	 * The International Standard of Industrial Classification of All Economic Activities (ISIC), Revision 4 code for a particular organization, business person, or place.
	 */
	public String isicV4;

	/**
	 * The job title of the person (for example, Financial Manager).
	 */
	public String jobTitle;

	/**
	 * The most generic bi-directional social/work relation.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> knows;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a topic that is known about - suggesting possible expertise but not implying it. We do not distinguish skill levels here, or relate this to educational content, events, objectives or <a class="localLink" href="https://schema.org/JobPosting">JobPosting</a> descriptions.
	 * (Expanded subclass of Thing to literal association)
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> knowsAboutPerson;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a topic that is known about - suggesting possible expertise but not implying it. We do not distinguish skill levels here, or relate this to educational content, events, objectives or <a class="localLink" href="https://schema.org/JobPosting">JobPosting</a> descriptions.
	 * (Expanded subclass of Thing to literal association)
	 */
	@ExportableToListOf(clazz=Organization.class)
	public List<Organization> knowsAboutOrganization;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a known language. We do not distinguish skill levels or reading/writing/speaking/signing here. Use language codes from the <a href="http://tools.ietf.org/html/bcp47">IETF BCP 47 standard</a>.
	 */
	public String knowsLanguage;

	/**
	 * An Organization (or ProgramMembership) to which this Person or Organization belongs.
	 */
	@ExportableToListOf(clazz=Organization.class)
	public List<Organization> member = new ArrayList<>();

	/**
	 * The North American Industry Classification System (NAICS) code for a particular organization or business person.
	 */
	public String naics;

	/**
	 * Nationality of the person.
	 */
	public String nationality;

	/**
	 * A parent of this person.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> parent;

	/**
	 * The most generic familial relation.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> relatedTo;

	/**
	 * A sibling of the person.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> sibling;

	/**
	 * A person or organization that supports a thing through a pledge, promise, or financial contribution. e.g. a sponsor of a Medical Study or a corporate sponsor of an event.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> sponsor;

	/**
	 * The person's spouse.
	 */
	@ExportableToListOf(clazz=Person.class)
	public List<Person> spouse;

	/**
	 * The Tax / Fiscal ID of the organization or person, e.g. the TIN in the US or the CIF/NIF in Spain.
	 */
	public String taxID;

	/**
	 * The telephone number.
	 */
	public String telephone;

	/**
	 * The Value-added Tax ID of the organization or person.
	 */
	public String vatID;

	/**
	 * The weight of the product or person.
	 */
	public BigInteger weight;

	/**
	 * A contact location for a person's place of work.
	 */
	@ExportableToListOf(clazz=Place.class)
	public List<Place> workLocation;

	/**
	 * Organizations that the person works for.
	 */
	@ExportableToListOf(clazz=Organization.class)
	public List<Organization> worksFor;

	/**
	 * Date of birth.
	 */
	public ZonedDateTime birthDate;

	/**
	 * Date of death.
	 */
	public ZonedDateTime deathDate;

	/**
	 * Where to find the definition of the OWL Class used to generate this Java class.
	 */
	public static String isDefinedBy = "https://schema.org/Person";
}

