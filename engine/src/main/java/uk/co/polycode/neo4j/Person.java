package uk.co.polycode.neo4j;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Person
 *
 * A person (alive, dead, undead, or fictional).
 *
 *
 *             This file was generated by OWL to Java as a transformation of the Schema.org schema Version 14.0.
 *             Schema.org is released under the Creative Commons Attribution-ShareAlike License (version 3.0).
 *             The Schema.org license is applicable to the generated source files and the license is available from
 *             <a href="https://creativecommons.org/licenses/by-sa/3.0/">...</a>
 *
 */
@Node
public class Person { // extends Thing {

	/**
	 * The synthetic key for this item.
	 */
	@Id
	@GeneratedValue // TODO: generate UUID and check best version to use. (generatorRef = "uuid")
	public UUID id;

	/**
	 * A Person is a Thing
	 */
	public Thing thing;

	/**
	 * An additional name for a Person, can be used for a middle name.
	 */
	public String additionalName;

	/**
	 * Physical address of the item.
	 */
	public PostalAddress address;

	/**
	 * An organization that this person is affiliated with. For example, a school/university, a club, or a team.
	 */
	public Organization affiliation;

	/**
	 * An organization that the person is an alumni of.
	 */
	public Organization alumniOfOrganization;

	/**
	 * An organization that the person is an alumni of.
	 */
	public Organization alumniOf;

	/**
	 * An award won by or for this item.
	 */
	public String award;

	/**
	 * The place where the person was born.
	 */
	@Relationship(type = "BIRTH_PLACEHAS", direction = Relationship.Direction.OUTGOING)
	public Place birthPlace;

	/**
	 * The brand(s) associated with a product or service, or the brand(s) maintained by an organization or business person.
	 */
	public Organization brand;

	/**
	 * A <a href="https://en.wikipedia.org/wiki/Call_sign">callsign</a>, as used in broadcasting and radio communications to identify people, radio and TV stations, or vehicles.
	 */
	public String callSign;

	/**
	 * A child of the person.
	 */
	public Person children;

	/**
	 * A colleague of the person.
	 */
	public Person colleague;

	/**
	 * The place where the person died.
	 */
	public Place deathPlace;

	/**
	 * The Dun &amp; Bradstreet DUNS number for identifying an organization or business person.
	 */
	public String duns;

	/**
	 * Email address.
	 */
	public String email;

	/**
	 * Family name. In the U.S., the last name of a Person.
	 */
	public String familyName;

	/**
	 * The fax number.
	 */
	public String faxNumber;

	/**
	 * The most generic uni-directional social relation.
	 */
	public Person follows;

	/**
	 * A person or organization that supports (sponsors) something through some kind of financial contribution.
	 */
	public Organization funderOrganization;

	/**
	 * A person or organization that supports (sponsors) something through some kind of financial contribution.
	 */
	public Person funder;

	/**
	 * Gender of something, typically a <a class="localLink" href="https://schema.org/Person">Person</a>, but possibly also fictional characters, animals, etc. While https://schema.org/Male and https://schema.org/Female may be used, text strings are also acceptable for people who do not identify as a binary gender. The <a class="localLink" href="https://schema.org/gender">gender</a> property can also be used in an extended sense to cover e.g. the gender of sports teams. As with the gender of individuals, we do not try to enumerate all possibilities. A mixed-gender <a class="localLink" href="https://schema.org/SportsTeam">SportsTeam</a> can be indicated with a text value of "Mixed".
	 */
	public String gender;

	/**
	 * Given name. In the U.S., the first name of a Person.
	 */
	public String givenName;

	/**
	 * The <a href="http://www.gs1.org/gln">Global Location Number</a> (GLN, sometimes also referred to as International Location Number or ILN) of the respective organization, person, or place. The GLN is a 13-digit number used to identify parties and physical locations.
	 */
	public String globalLocationNumber;

	/**
	 * A credential awarded to the Person or Organization.
	 */
	public String hasCredential;

	/**
	 * The Person's occupation. For past professions, use Role for expressing dates.
	 */
	public String hasOccupation;

	/**
	 * Points-of-Sales operated by the organization or person.
	 */
	public Place hasPOS;

	/**
	 * The height of the item.
	 */
	public BigInteger height;

	/**
	 * A contact location for a person's residence.
	 */
	public Place homeLocation;

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
	public Person knows;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a topic that is known about - suggesting possible expertise but not implying it. We do not distinguish skill levels here, or relate this to educational content, events, objectives or <a class="localLink" href="https://schema.org/JobPosting">JobPosting</a> descriptions.
	 */
	public Thing knowsAbout;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a known language. We do not distinguish skill levels or reading/writing/speaking/signing here. Use language codes from the <a href="http://tools.ietf.org/html/bcp47">IETF BCP 47 standard</a>.
	 */
	public String knowsLanguage;

	/**
	 * An Organization (or ProgramMembership) to which this Person or Organization belongs.
	 */
	public Organization memberOfOrganization;

	/**
	 * An Organization (or ProgramMembership) to which this Person or Organization belongs.
	 */
	public Organization memberOf;

	/**
	 * The North American Industry Classification System (NAICS) code for a particular organization or business person.
	 */
	public String naics;

	/**
	 * Nationality of the person.
	 */
	public String nationality;

	/**
	 * The total financial value of the person as calculated by subtracting assets from liabilities.
	 */
	public BigDecimal netWorth;

	/**
	 * A parent of this person.
	 */
	public Person parent;

	/**
	 * The most generic familial relation.
	 */
	public Person relatedTo;

	/**
	 * A sibling of the person.
	 */
	public Person sibling;

	/**
	 * A person or organization that supports a thing through a pledge, promise, or financial contribution. e.g. a sponsor of a Medical Study or a corporate sponsor of an event.
	 */
	public Organization sponsorOrganization;

	/**
	 * A person or organization that supports a thing through a pledge, promise, or financial contribution. e.g. a sponsor of a Medical Study or a corporate sponsor of an event.
	 */
	public Person sponsor;

	/**
	 * The person's spouse.
	 */
	public Person spouse;

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
	public Place workLocation;

	/**
	 * Organizations that the person works for.
	 */
	public Organization worksFor;

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
	public String isDefinedBy = "https://schema.org/Person";
}

