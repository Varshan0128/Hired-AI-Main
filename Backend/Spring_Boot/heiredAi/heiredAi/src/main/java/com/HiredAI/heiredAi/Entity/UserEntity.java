package com.HiredAI.heiredAi.Entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class UserEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userName;
	private String email;
	private String password;
	
	private String firstName;  
    private String lastName;   
    private String mobile; 
    
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime lastLoginAt;
    private String acquisitionSource;
    
    private String otp;
    private long otpExpiry; 
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified;
   // private String verificationToken;
	@ElementCollection
	@CollectionTable(name = "user_dream_jobs", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "dream_job")
	private List<String> dreamJobs;
	 
	
	public boolean isVerified() {
		return verified;
	}
	public List<String> getDreamJobs() {
		return dreamJobs;
	}
	public void setDreamJobs(List<String> dreamJobs) {
		this.dreamJobs = dreamJobs;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public long getOtpExpiry() {
		return otpExpiry;
	}
	public void setOtpExpiry(long otpExpiry) {
		this.otpExpiry = otpExpiry;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserEntity(Long id, String userName, String email, String password, String firstName, String lastName,
			String mobile) {
		super();
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * Creates an empty user entity.
	 */
	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Gets the time when this user record was created.
	 *
	 * @return the creation timestamp
	 */
	public java.time.LocalDateTime getCreatedAt() {
		return createdAt;
	}
	/**
	 * Sets the time this user record was created.
	 *
	 * @param createdAt the creation time
	 */
	public void setCreatedAt(java.time.LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	/**
	 * Gets the time of the user's most recent login.
	 *
	 * @return the timestamp of the last login
	 */
	public java.time.LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}
	/**
	 * Sets the time of the user's most recent login.
	 *
	 * @param lastLoginAt the timestamp of the last login
	 */
	public void setLastLoginAt(java.time.LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}
	/**
	 * Gets the source that was used to acquire the user.
	 *
	 * @return the acquisition source
	 */
	public String getAcquisitionSource() {
		return acquisitionSource;
	}
	/**
	 * Sets the user's acquisition source.
	 *
	 * @param acquisitionSource the source associated with the user
	 */
	public void setAcquisitionSource(String acquisitionSource) {
		this.acquisitionSource = acquisitionSource;
	}
	
	/**
	 * Sets the creation timestamp before the entity is first persisted.
	 */
	@jakarta.persistence.PrePersist
	protected void onCreate() {
		this.createdAt = java.time.LocalDateTime.now();
	}
}
