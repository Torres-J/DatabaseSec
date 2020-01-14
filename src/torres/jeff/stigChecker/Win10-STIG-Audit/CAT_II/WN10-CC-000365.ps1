## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-94719"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = "Completed"
#$Regkey = (Get-ItemProperty Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\OneDrive\AllowTenantList\")."1111-2222-3333-4444"
#if ($regkey -eq '1111-2222-3333-4444'){
#    $Configuration = 'PASS V-94719: OneDrive only allows synchronizing of accounts for DoD organization instances'}
#    else{
#    $Configuration = 'FAIL V-94719: OneDrive allows synchronizing of accounts outside of DoD organization instances'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit