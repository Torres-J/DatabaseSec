## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-1089"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\policies\system").LegalNoticeText | out-file $env:temp/result1089.txt | out-null
$CFG = $(gc C:\Users\ALEXC~1.BLA\AppData\Local\Temp\result1089.txt)

if ($CFG -contains "You are accessing a U.S. Government (USG) Information System (IS) that is provided for USG-authorized use only." -and "By using this IS (which includes any device attached to this IS), you consent to the following conditions:" -and "-The USG routinely intercepts and monitors communications on this IS for purposes including, but not limited to, penetration testing, COMSEC monitoring, network operations and defense, personnel misconduct (PM), law enforcement (LE), and counterintelligence (CI) investigations." -and "-At any time, the USG may inspect and seize data stored on this IS." -and "-Communications using, or data stored on, this IS are not private, are subject to routine monitoring, interception, and search, and may be disclosed or used for any USG-authorized purpose." -and "-This IS includes security measures (e.g., authentication and access controls) to protect USG interests--not for your personal benefit or privacy." -and "-Notwithstanding the above, using this IS does not constitute consent to PM, LE or CI investigative searching or monitoring of the content of privileged communications, or work product, related to personal representation or services by attorneys, psychotherapists, or clergy, and their assistants.  Such communications and work product are private and confidential.  See User Agreement for details.")                                                                                                     
{
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit